package ca.uqac.friendschallenge.viewmodel

import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.data.AuthState
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.FirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val firebaseHelper = FirebaseHelper()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        firebaseHelper.checkAuthStatus { userId ->
            _isLoading.value = false
            if (userId == null) {
                _authState.update { it.copy(isAuthenticated = false) }
            } else {
                fetchUser(userId)
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) return

        _authState.update { it.copy(isLoading = true) }
        firebaseHelper.login(email, password) { result ->
            result.onSuccess { uid ->
                fetchUser(uid)
            }
            result.onFailure { exception ->
                handleError(exception.localizedMessage)
            }
        }
    }

    fun register(email: String, password: String, username: String) {
        if (email.isBlank() || password.isBlank() || username.isBlank()) {
            handleError("Please fill in all the fields")
            return
        }

        _authState.update { it.copy(isLoading = true) }
        firebaseHelper.register(email, password, username) { result ->
            result.onSuccess { user ->
                saveUserInFirestore(user)
            }
            result.onFailure { exception ->
                handleError(exception.localizedMessage)
            }
        }
    }

    fun logout() {
        firebaseHelper.logout()
        _authState.update { it.copy(isAuthenticated = false, userModel = null) }
    }

    private fun fetchUser(userId: String) {
        firebaseHelper.fetchUser(userId) { result ->
            result.onSuccess { user ->
                _authState.update {
                    it.copy(
                        isAuthenticated = true,
                        isLoading = false,
                        userModel = user,
                        errorMessage = null
                    )
                }
            }
            result.onFailure { exception ->
                handleError(exception.localizedMessage)
            }
        }
    }

    private fun saveUserInFirestore(user: UserModel) {
        firebaseHelper.saveUser(user) { result ->
            result.onSuccess {
                _authState.update {
                    it.copy(
                        isAuthenticated = true,
                        isLoading = false,
                        userModel = user,
                        errorMessage = null
                    )
                }
            }
            result.onFailure { exception ->
                handleError(exception.localizedMessage)
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
}
