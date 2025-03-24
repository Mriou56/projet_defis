package ca.uqac.friendschallenge.model

import com.google.firebase.Timestamp

data class FriendModel(
    val friendId: String,
    val friendName: String,
    val status: FriendStatus,
    val sentBy: String,
    val createdAt: Timestamp
)

enum class FriendStatus {
    PENDING,
    ACCEPTED,
    REQUESTED,
    REJECTED
}