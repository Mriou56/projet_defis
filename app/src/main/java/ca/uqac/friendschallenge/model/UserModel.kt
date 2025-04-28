package ca.uqac.friendschallenge.model

// This data class represents a user in the application.
data class UserModel(
    val uid: String,
    val username: String,
    val email: String,
    val totalScore: Double,
    val scoreWeek: Double,
)