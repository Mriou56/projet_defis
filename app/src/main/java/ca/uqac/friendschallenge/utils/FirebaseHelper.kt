package ca.uqac.friendschallenge.utils

import android.graphics.Bitmap
import ca.uqac.friendschallenge.model.ChallengeModel
import ca.uqac.friendschallenge.model.ChallengeStatus
import ca.uqac.friendschallenge.model.FriendModel
import ca.uqac.friendschallenge.model.FriendStatus
import ca.uqac.friendschallenge.model.ImageModel
import ca.uqac.friendschallenge.model.ParticipationModel
import ca.uqac.friendschallenge.model.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

/**
 * Helper class for Firebase operations.
 */
class FirebaseHelper() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    /**
     * Checks the authentication status of the user.
     *
     * @param callback A callback function to handle the result.
     */
    fun checkAuthStatus(callback: (String?) -> Unit) {
        val currentUser = auth.currentUser
        callback(currentUser?.uid)
    }

    /**
     * Logs in the user with the provided email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @param callback A callback function to handle the result.
     */
    fun login(email: String, password: String, callback: (Result<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.success(auth.currentUser?.uid ?: ""))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Login failed")))
                }
            }
    }

    /**
     * Registers a new user with the provided email, password, and username.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @param username The username of the user.
     * @param callback A callback function to handle the result.
     */
    fun register(
        email: String,
        password: String,
        username: String,
        callback: (Result<UserModel>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = UserModel(auth.currentUser?.uid ?: "", username, email, 0.0, 0.0)
                    callback(Result.success(user))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Registration failed")))
                }
            }
    }

    /**
     * Logs out the current user.
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Fetches the user data from Firestore.
     *
     * @param userId The ID of the user.
     * @param callback A callback function to handle the result.
     */
    fun fetchUser(userId: String, callback: (Result<UserModel>) -> Unit) {
        firestore.collection("users").document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { document ->
                        val user = document.toUserModel()
                        if (user != null) {
                            callback(Result.success(user))
                        } else {
                            callback(Result.failure(Exception("Failed to parse user data")))
                        }
                    } ?: callback(Result.failure(Exception("User not found")))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Failed to fetch user")))
                }
            }
    }

    /**
     * Fetches the list of friends for the current user.
     *
     * @param callback A callback function to handle the result.
     */
    fun fetchUserFriends(callback: (Result<List<FriendModel>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users").document(userId).collection("friends")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val friends = task.result?.mapNotNull { it.toFriendModel() }
                    if (friends != null) {
                        callback(Result.success(friends))
                    } else {
                        callback(Result.failure(Exception("Failed to parse friends data")))
                    }
                } else {
                    callback(Result.failure(task.exception ?: Exception("Failed to fetch friends")))
                }
            }
    }

    /**
     * Saves the user data to Firestore.
     *
     * @param user The user data to save.
     * @param callback A callback function to handle the result.
     */
    fun saveUser(user: UserModel, callback: (Result<Unit>) -> Unit) {
        firestore.collection("users").document(user.uid)
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.success(Unit))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Failed to save user")))
                }
            }
    }

    /**
     * Searches for users by username.
     *
     * @param query The search query.
     * @param callback A callback function to handle the result.
     */
    fun searchUsersByUsername(query: String, callback: (Result<List<UserModel>>) -> Unit) {
        firestore.collection("users")
            .whereGreaterThanOrEqualTo("username", query)
            .whereLessThanOrEqualTo("username", query + '\uf8ff')
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.mapNotNull { it.toUserModel() }
                callback(Result.success(users))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    /**
     * Sends a friend request to another user.
     *
     * @param userId The ID of the user sending the request.
     * @param friendId The ID of the user receiving the request.
     * @param friendModel The friend model data.
     * @param callback A callback function to handle the result.
     */
    private fun userFriendDoc(userId: String, friendId: String) =
        firestore.collection("users").document(userId).collection("friends").document(friendId)

    /**
     * Performs a friend operation (add or remove).
     *
     * @param user1Id The ID of the first user.
     * @param user2Id The ID of the second user.
     * @param user1Model The friend model data for the first user.
     * @param user2Model The friend model data for the second user.
     * @param callback A callback function to handle the result.
     */
    fun performFriendOperation(
        user1Id: String,
        user2Id: String,
        user1Model: FriendModel,
        user2Model: FriendModel,
        callback: (Result<Unit>) -> Unit
    ) {
        val user1Task = userFriendDoc(user1Id, user2Id).set(user1Model)

        user1Task.addOnCompleteListener { task1 ->
            if (task1.isSuccessful) {
                val user2Task = userFriendDoc(user2Id, user1Id).set(user2Model)

                user2Task.addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        callback(Result.success(Unit))
                    } else {
                        callback(
                            Result.failure(
                                task2.exception ?: Exception("Erreur côté destinataire")
                            )
                        )
                    }
                }
            } else {
                callback(Result.failure(task1.exception ?: Exception("Erreur côté expéditeur")))
            }
        }
    }

    /**
     * Checks if it's time to vote.
     *
     * @param callback A callback function to handle the result.
     */
    fun isTimeToVote(callback: (Boolean) -> Unit) {
        firestore.collection("appsettings").document("settings")
            .get()
            .addOnSuccessListener { document ->
                val data = document.data
                val isTimeToVote = data?.getBoolean("isTimeToVote") ?: false

                callback(isTimeToVote)
            }.addOnFailureListener {
                callback(false)
            }
    }

    /**
     * Fetches the weekly challenge from Firestore.
     *
     * @param callback A callback function to handle the result.
     */
    fun getWeeklyChallenge(callback: (Result<ChallengeModel>) -> Unit) {
        firestore.collection("challenges")
            .whereEqualTo("status", ChallengeStatus.WEEKLY.name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    callback(Result.failure(Exception("No weekly challenge found")))
                    return@addOnSuccessListener
                }

                val challenge = querySnapshot.documents.firstOrNull()?.toChallengeModel()

                if (challenge != null) {
                    callback(Result.success(challenge))
                } else {
                    callback(Result.failure(Exception("Failed to parse challenge data")))
                }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    /**
     * Submits a challenge participation with an image.
     *
     * @param bitmap The image bitmap to upload.
     * @param challenge The challenge data.
     * @param user The user data.
     * @param callback A callback function to handle the result.
     */
    fun submitChallengeParticipation(
        bitmap: Bitmap,
        challenge: ChallengeModel,
        user: UserModel,
        callback: (Result<String>) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return

        // Convert Bitmap to ByteArray
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageData = byteArrayOutputStream.toByteArray()

        // Upload the photo to Firebase Storage
        val imageId = firestore.collection("images").document().id
        val storageRef = storage.getReference("images/$imageId.jpg")
        val uploadTask = storageRef.putBytes(imageData)
        uploadTask
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Upload failed")
                }
                storageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                val data = mapOf(
                    "imageUrl" to downloadUri.toString(),
                    "username" to user.username,
                )

                // Create a new participation document for the challenge
                firestore.collection("challenges").document(challenge.id)
                    .collection("participation")
                    .document(userId)
                    .set(data)
                    .addOnSuccessListener {
                        val dataChallengeUser = mapOf(
                            "image" to downloadUri.toString(),
                            "challengeTitle" to challenge.title,
                            "challengeId" to challenge.id,
                            "createdAt" to Timestamp.now()
                        )

                        val userDocRef = firestore.collection("users").document(userId)

                        userDocRef.collection("challenges")
                            .document(challenge.id)
                            .set(dataChallengeUser)
                            .addOnSuccessListener {
                                callback(Result.success(downloadUri.toString()))
                            }
                            .addOnFailureListener {
                                callback(Result.failure(it))
                            }

                    }
                    .addOnFailureListener {
                        callback(Result.failure(it))
                    }
            }
            .addOnFailureListener { callback(Result.failure(it)) }
    }

    /**
     * Deletes a participation from a challenge.
     *
     * @param challengeId The ID of the challenge.
     * @param callback A callback function to handle the result.
     */
    fun deleteParticipation(challengeId: String, callback: (Result<Unit>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("challenges").document(challengeId)
            .collection("participation").document(userId)
            .delete()
            .addOnSuccessListener {
                firestore.collection("users").document(userId)
                    .collection("challenges").document(challengeId)
                    .delete()
                    .addOnSuccessListener {
                        callback(Result.success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        callback(Result.failure(exception))
                    }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    /**
     * Fetches the images of the user.
     *
     * @param callback A callback function to handle the result.
     */
    fun fetchUserImages(callback: (Result<List<ImageModel>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users").document(userId).collection("challenges")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val images = querySnapshot.documents.mapNotNull { document ->
                    val imageUrl = document.getString("image")
                    val title = document.getString("challengeTitle") ?: ""
                    val id = document.id

                    imageUrl?.let {
                        ImageModel(
                            id = id,
                            imageUrl = it,
                            userId = userId,
                            challengeTitle = title,
                        )
                    }
                }
                callback(Result.success(images))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    /**
     * Fetches the participations of friends in a challenge.
     *
     * @param challengeId The ID of the challenge.
     * @param userId The ID of the user.
     * @param callback A callback function to handle the result.
     */
    fun getParticipationsOfFriends(
        challengeId: String,
        userId: String,
        callback: (Result<List<ParticipationModel>>) -> Unit
    ) {
        firestore.collection("users").document(userId)
            .collection("friends")
            .whereEqualTo("status", FriendStatus.ACCEPTED.name)
            .get()
            .addOnSuccessListener { amisSnapshot ->
                val amisIds = amisSnapshot.documents.mapNotNull { it.id }
                firestore.collection("challenges").document(challengeId)
                    .collection("participation")
                    .get()
                    .addOnSuccessListener { participationsSnapshot ->
                        val allParticipations = participationsSnapshot.documents.mapNotNull { doc ->
                            doc.toParticipationModel()?.copy(id = doc.id)
                        }
                        val participationsFiltrees = allParticipations.filter { it.id in amisIds }
                        callback(Result.success(participationsFiltrees))
                    }
                    .addOnFailureListener {
                        callback(Result.failure(it))
                    }
            }
            .addOnFailureListener {
                callback(Result.failure(it))
            }
    }

    /**
     * Votes for an image in a challenge.
     *
     * @param rating The rating to give to the image.
     * @param challengeId The ID of the challenge.
     * @param participationId The ID of the participation.
     * @param callback A callback function to handle the result.
     */
    fun voteForImage(
        rating: Float,
        challengeId: String,
        participationId: String,
        callback: (Result<Unit>) -> Unit
    ) {
        val userID = auth.currentUser?.uid ?: return

        val data = mapOf(
            "idVotant" to userID,
            "note" to rating
        )

        firestore.collection("challenges").document(challengeId)
            .collection("participation").document(participationId)
            .collection("votes").document(userID)
            .set(data)
            .addOnSuccessListener {
                callback(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }

    }

    /**
     * Marks the voting as completed for a user.
     *
     * @param userId The ID of the user.
     * @param callback A callback function to handle the result.
     */
    fun markVotingCompleted(userId: String, callback: (Result<Unit>) -> Unit) {
        val data = mapOf("voted" to true)

        firestore.collection("users")
            .document(userId)
            .update(data)
            .addOnSuccessListener { callback(Result.success(Unit)) }
            .addOnFailureListener { exception -> callback(Result.failure(exception)) }
    }

    /**
     * Checks if the user has already voted.
     *
     * @param userId The ID of the user.
     * @param onResult A callback function to handle the result.
     */
    fun checkIfUserVoted(userId: String, onResult: (Boolean) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val voted = document.getBoolean("voted") ?: false
                onResult(voted)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    /**
     * Fetches the list of challenges from Firestore.
     *
     * @param callback A callback function to handle the result.
     */
    private fun Map<String, Any?>.getString(key: String): String? = this[key] as? String

    /**
     * Fetches a double value from the map.
     *
     * @param key The key to fetch the value for.
     * @return The double value associated with the key, or null if not found.
     */
    private fun Map<String, Any?>.getTimestamp(key: String): Timestamp? = this[key] as? Timestamp

    /**
     * Fetches a double value from the map.
     *
     * @param key The key to fetch the value for.
     * @return The double value associated with the key, or null if not found.
     */
    private fun Map<String, Any?>.getBoolean(key: String): Boolean? = this[key] as? Boolean

    /**
     * Fetches a double value from the map.
     *
     * @param key The key to fetch the value for.
     * @return The double value associated with the key, or null if not found.
     */
    private fun com.google.firebase.firestore.DocumentSnapshot.toChallengeModel(): ChallengeModel? {
        val data = data ?: return null
        val id = id
        val title = data.getString("title") ?: return null
        val status = data.getString("status")?.let { ChallengeStatus.valueOf(it) } ?: return null
        return ChallengeModel(id, title, status)
    }

    /**
     * Converts a Firestore document snapshot to a ParticipationModel.
     *
     * @return The ParticipationModel object, or null if conversion fails.
     */
    private fun com.google.firebase.firestore.DocumentSnapshot.toParticipationModel(): ParticipationModel? {
        val data = data ?: return null
        val username = data.getString("username") ?: return null
        val imageUrl = data.getString("imageUrl") ?: return null
        return ParticipationModel(id, username, imageUrl)
    }

    /**
     * Converts a Firestore document snapshot to a UserModel.
     *
     * @return The UserModel object, or null if conversion fails.
     */
    private fun com.google.firebase.firestore.QueryDocumentSnapshot.toUserModel(): UserModel? {
        val data = data
        val userId = data.getString("uid") ?: return null
        val username = data.getString("username") ?: return null
        val email = data.getString("email") ?: return null
        val totalScore = getDouble("totalScore") ?: 0.0
        val scoreSemaine = getDouble("scoreSemaine") ?: 0.0
        return UserModel(userId, username, email, totalScore, scoreSemaine)
    }

    /**
     * Converts a Firestore document snapshot to a UserModel.
     *
     * @return The UserModel object, or null if conversion fails.
     */
    private fun com.google.firebase.firestore.DocumentSnapshot.toUserModel(): UserModel? {
        val data = data ?: return null
        val uid = data.getString("uid") ?: return null
        val username = data.getString("username") ?: return null
        val email = data.getString("email") ?: return null
        val totalScore = getDouble("totalScore") ?: 0.0
        val scoreSemaine = getDouble("scoreWeek") ?: 0.0
        return UserModel(uid, username, email, totalScore, scoreSemaine)
    }

    /**
     * Converts a Firestore document snapshot to a FriendModel.
     *
     * @return The FriendModel object, or null if conversion fails.
     */
    private fun com.google.firebase.firestore.QueryDocumentSnapshot.toFriendModel(): FriendModel? {
        val data = data
        val friendId = data.getString("friendId") ?: return null
        val friendName = data.getString("friendName") ?: return null
        val status = data.getString("status")?.let { FriendStatus.valueOf(it) } ?: return null
        val sentBy = data.getString("sentBy") ?: return null
        val createdAt = data.getTimestamp("createdAt") ?: return null
        val totalScore = getDouble("totalScore") ?: 0.0
        val scoreSemaine = getDouble("scoreWeek") ?: 0.0

        return FriendModel(
            friendId,
            friendName,
            status,
            sentBy,
            createdAt,
            totalScore,
            scoreSemaine
        )
    }
}
