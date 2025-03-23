package ca.uqac.friendschallenge.viewmodel

import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.data.AuthState
import ca.uqac.friendschallenge.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _authState.update { it.copy(isAuthenticated = false) }
        } else {
            fetchUser(currentUser.uid)
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return

        _authState.update { it.copy(isLoading = true) }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.uid?.let { fetchUser(it) }
                } else {
                    handleError(task.exception?.localizedMessage)
                }
            }
    }

    fun register(email: String, password: String, username: String) {
        if (email.isBlank() || password.isBlank() || username.isBlank()) {
            handleError("Please fill in all the fields")
            return
        }

        _authState.update { it.copy(isLoading = true) }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                    val userModel = UserModel(userId, username, email)
                    saveUserInFirestore(userModel)
                } else {
                    handleError(task.exception?.localizedMessage)
                }
            }
    }

    fun logout() {
        auth.signOut()
        _authState.update { it.copy(isAuthenticated = false, userModel = null) }
    }

    private fun fetchUser(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { document ->
                        val user = document.toUserModel()
                        if (user != null) {
                            _authState.update {
                                it.copy(
                                    isAuthenticated = true,
                                    isLoading = false,
                                    userModel = user,
                                    errorMessage = null
                                )
                            }
                        } else {
                            handleError("Failed to parse user data")
                        }
                    }
                } else {
                    handleError(task.exception?.localizedMessage)
                }
            }
    }

    private fun saveUserInFirestore(user: UserModel) {
        firestore.collection("users").document(user.uid)
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.update {
                        it.copy(
                            isAuthenticated = true,
                            isLoading = false,
                            userModel = user,
                            errorMessage = null
                        )
                    }
                } else {
                    handleError(task.exception?.localizedMessage)
                }
            }
    }

    private fun handleError(message: String?) {
        _authState.update {
            it.copy(
                errorMessage = message ?: "Something went wrong",
                isLoading = false
            )
        }
    }

    // Helper functions to parse Firestore data
    private fun Map<String, Any?>.getString(key: String): String? = this[key] as? String

    private fun com.google.firebase.firestore.DocumentSnapshot.toUserModel(): UserModel? {
        val data = data ?: return null
        val uid = data.getString("uid") ?: return null
        val username = data.getString("username") ?: return null
        val email = data.getString("email") ?: return null
        return UserModel(uid, username, email)
    }
}
