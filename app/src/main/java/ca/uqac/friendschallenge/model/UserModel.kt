package ca.uqac.friendschallenge.model

data class UserModel(
    val uid: String,
    val username: String,
    val email: String,
    val totalScore: Double,
    val scoreSemaine: Double,
)