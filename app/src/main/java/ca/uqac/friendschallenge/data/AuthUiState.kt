package ca.uqac.friendschallenge.data

import ca.uqac.friendschallenge.model.UserModel

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userModel: UserModel? = null
)