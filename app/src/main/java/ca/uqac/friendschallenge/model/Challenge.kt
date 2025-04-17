package ca.uqac.friendschallenge.model

data class Challenge (
    val id: String,
    val title: String,
    val status: ChallengeStatus,
)

enum class ChallengeStatus {
    WEEKLY,
    DID,
    NO,
}