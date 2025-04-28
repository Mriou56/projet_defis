package ca.uqac.friendschallenge.viewmodel

import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.data.AuthState
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.FirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel class for managing authentication state and user data.
 *
 * @property firebaseHelper An instance of FirebaseHelper to interact with Firebase services.
 */
class AuthViewModel : ViewModel() {

    private val firebaseHelper = FirebaseHelper()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        checkAuthStatus()
    }

    /**
     * Checks the authentication status of the user.
     * If the user is authenticated, fetches the user data.
     * If not, sets the authentication state to false.
     */
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

    /**
     * Logs in the user with the provided email and password.
     * If successful, fetches the user data.
     * If failed, updates the authentication state with an error message.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     */
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

    /**
     * Registers a new user with the provided email, password, and username.
     * If successful, saves the user data in Firestore.
     * If failed, updates the authentication state with an error message.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @param username The username of the user.
     */
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

    /**
     * Logs out the user and updates the authentication state.
     */
    fun logout() {
        firebaseHelper.logout()
        _authState.update { it.copy(isAuthenticated = false, userModel = null) }
    }

    /**
     * Fetches the user data from Firestore using the provided user ID.
     * If successful, updates the authentication state with the user data.
     * If failed, updates the authentication state with an error message.
     *
     * @param userId The ID of the user to fetch.
     */
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

    /**
     * Saves the user data in Firestore.
     * If successful, updates the authentication state with the user data.
     * If failed, updates the authentication state with an error message.
     *
     * @param user The user data to save.
     */
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

    /**
     * Handles errors by updating the authentication state with an error message.
     *
     * @param message The error message to display.
     */
    private fun handleError(message: String?) {
        _authState.update {
            it.copy(
                errorMessage = message ?: "Something went wrong",
                isLoading = false
            )
        }
    }
}
