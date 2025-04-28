package ca.uqac.friendschallenge.model

// This data class represents an image in the application.
data class ImageModel (
    val id: String,
    val imageUrl: String,
    val userId: String,
    val challengeTitle: String,
)

