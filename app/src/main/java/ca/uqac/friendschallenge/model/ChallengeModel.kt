package ca.uqac.friendschallenge.model

// This data class represents a challenge in the application.
data class ChallengeModel(
    val id: String,
    val title: String,
    val status: ChallengeStatus,
)

// This enum class represents the possible statuses of a challenge.
enum class ChallengeStatus {
    WEEKLY,
    DID,
    NO,
}