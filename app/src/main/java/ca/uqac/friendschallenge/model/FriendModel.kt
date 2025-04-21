package ca.uqac.friendschallenge.model

import com.google.firebase.Timestamp

data class FriendModel(
    val friendId: String,
    val friendName: String,
    val status: FriendStatus,
    val sentBy: String,
    val createdAt: Timestamp,
    val totalScore: Double,
    val scoreSemaine: Double,
)

enum class FriendStatus {
    PENDING,
    ACCEPTED,
    REQUESTED,
    REJECTED
}