package ca.uqac.friendschallenge.model

data class ImageModel (
    val id: String,
    val imageUrl: String,
    val userId: String,
    val consigne_defi: String,

    // Revoir si on a besoin de ces champs
    //val createdAt: String,
    //val updatedAt: String
)

