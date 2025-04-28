package ca.uqac.friendschallenge.data

import ca.uqac.friendschallenge.model.UserModel

// This data class represents the authentication state of the user in the application.
data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userModel: UserModel? = null
)