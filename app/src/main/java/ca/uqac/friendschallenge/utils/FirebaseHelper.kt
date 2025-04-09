package ca.uqac.friendschallenge.utils

import android.graphics.Bitmap
import android.util.Log
import ca.uqac.friendschallenge.model.DefiModel
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

class FirebaseHelper() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun checkAuthStatus(callback: (String?) -> Unit) {
        val currentUser = auth.currentUser
        callback(currentUser?.uid)
    }

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

    fun register(email: String, password: String, username: String, callback: (Result<UserModel>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = UserModel(auth.currentUser?.uid ?: "", username, email)
                    callback(Result.success(user))
                } else {
                    callback(Result.failure(task.exception ?: Exception("Registration failed")))
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

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

    private fun userFriendDoc(userId: String, friendId: String) =
        firestore.collection("users").document(userId).collection("friends").document(friendId)

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
                        callback(Result.failure(task2.exception ?: Exception("Erreur côté destinataire")))
                    }
                }
            } else {
                callback(Result.failure(task1.exception ?: Exception("Erreur côté expéditeur")))
            }
        }
    }

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


    fun getWeeklyDefi(callback: (Result<Pair<String, String>>) -> Unit) {
        firestore.collection("Defis")
            .get(com.google.firebase.firestore.Source.SERVER)
            .addOnSuccessListener { querySnapshot ->
                val weeklyDefi = querySnapshot.documents.firstOrNull { document ->
                    document.getString("realise") == "WEEKLY"
                }

                if (weeklyDefi != null) {
                    val defiId = weeklyDefi.id
                    val consigne = weeklyDefi.getString("consigne") ?: "Consigne introuvable"

                    callback(Result.success(Pair(defiId, consigne)))
                } else {
                    callback(Result.failure(Exception("Weekly challenge not found")))
                }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    fun uploadPhoto(bitmap: Bitmap, defi: DefiModel, user: UserModel, callback: (Result<String>) -> Unit) {
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

                firestore.collection("Defis").document(defi.id).collection("participation")
                    .document(userId)
                    .set(data)
                    .addOnSuccessListener {
                        val dataDefisUser = mapOf(
                            "image" to downloadUri.toString(),
                            "defiConsigne" to defi.consigne,
                            "defiId" to defi.id,
                            "createdAt" to Timestamp.now()
                        )

                        val userDocRef = firestore.collection("users").document(userId)

                        userDocRef.collection("defis")
                            .document(defi.id)
                            .set(dataDefisUser)
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

    fun fetchUserImages(callback: (Result<List<ImageModel>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users").document(userId).collection("defis")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val images = querySnapshot.documents.mapNotNull { document ->
                    val imageUrl = document.getString("image")
                    val consigne = document.getString("consigne_defi") ?: ""
                    val id = document.id

                    imageUrl?.let {
                        ImageModel(
                            id = id,
                            imageUrl = it,
                            userId = userId,
                            consigne_defi = consigne,
                        )
                    }
                }
                callback(Result.success(images))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    fun getParticipationsOfFriends(
        defiId: String,
        userId: String,
        callback: (Result<List<ParticipationModel>>) -> Unit
    ) {
        firestore.collection("users").document(userId)
            .collection("friends").get()
            .addOnSuccessListener { amisSnapshot ->
                val amisIds = amisSnapshot.documents.mapNotNull { it.id }

                Log.d("DEBUG", "Amis IDs récupérés : $amisIds")

                firestore.collection("Defis").document(defiId)
                    .collection("participations").get()
                    .addOnSuccessListener { participationsSnapshot ->
                        val allParticipations = participationsSnapshot.documents.map {
                            it.id to it.data
                        }

                        Log.d("DEBUG", "Toutes les participations trouvées : $allParticipations")

                        val participationsFiltrees = participationsSnapshot.documents
                            .filter { it.id in amisIds }
                            .mapNotNull { doc ->
                                doc.toObject(ParticipationModel::class.java)?.copy(userId = doc.id)
                            }

                        Log.d("DEBUG", "Participations des amis filtrées : $participationsFiltrees")

                        callback(Result.success(participationsFiltrees))
                    }
                    .addOnFailureListener {
                        Log.e("DEBUG", "Erreur lors de la récupération des participations : ", it)
                        callback(Result.failure(it))
                    }
            }
            .addOnFailureListener {
                Log.e("DEBUG", "Erreur lors de la récupération des amis : ", it)
                callback(Result.failure(it))
            }
    }


    fun voteForImage(
        rating: Float,
        userVotantId: String,
        defiId: String,
        participationId: String,
        callback: (Result<Unit>) -> Unit
    ) {
        val data = mapOf(
            "idVotant" to userVotantId,
            "note" to rating
        )

        firestore.collection("Defis").document(defiId)
            .collection("participations").document(participationId)
            .collection("votes")
            .add(data)
            .addOnSuccessListener {
                callback(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }

    }


    private fun Map<String, Any?>.getString(key: String): String? = this[key] as? String

    private fun Map<String, Any?>.getTimestamp(key: String): Timestamp? = this[key] as? Timestamp

    private fun Map<String, Any?>.getBoolean(key: String): Boolean? = this[key] as? Boolean

    private fun com.google.firebase.firestore.QueryDocumentSnapshot.toUserModel(): UserModel? {
        val data = data
        val userId = data.getString("uid") ?: return null
        val username = data.getString("username") ?: return null
        val email = data.getString("email") ?: return null
        return UserModel(userId, username, email)
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toUserModel(): UserModel? {
        val data = data ?: return null
        val uid = data.getString("uid") ?: return null
        val username = data.getString("username") ?: return null
        val email = data.getString("email") ?: return null
        return UserModel(uid, username, email)
    }

    private fun com.google.firebase.firestore.QueryDocumentSnapshot.toFriendModel(): FriendModel? {
        val data = data
        val friendId = data.getString("friendId") ?: return null
        val friendName = data.getString("friendName") ?: return null
        val status = data.getString("status")?.let { FriendStatus.valueOf(it) } ?: return null
        val sentBy = data.getString("sentBy") ?: return null
        val createdAt = data.getTimestamp("createdAt") ?: return null
        return FriendModel(friendId, friendName, status, sentBy, createdAt)
    }
}
