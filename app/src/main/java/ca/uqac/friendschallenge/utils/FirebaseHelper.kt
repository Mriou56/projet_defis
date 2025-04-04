package ca.uqac.friendschallenge.utils

import ca.uqac.friendschallenge.model.FriendModel
import ca.uqac.friendschallenge.model.FriendStatus
import ca.uqac.friendschallenge.model.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHelper() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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
