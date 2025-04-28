package ca.uqac.friendschallenge.model

import com.google.firebase.Timestamp

// This data class represents a friend in the application.
data class FriendModel(
    val friendId: String,
    val friendName: String,
    val status: FriendStatus,
    val sentBy: String,
    val createdAt: Timestamp,
    val totalScore: Double,
    val scoreWeek: Double,
)

// This enum represents the possible statuses of a friend request in the application.
enum class FriendStatus {
    PENDING,
    ACCEPTED,
    REQUESTED,
    REJECTED
}