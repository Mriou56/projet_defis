package ca.uqac.friendschallenge.model

import androidx.compose.runtime.State

data class DefiModel (
    val id: String,
    val consigne: String,
    val realise: String,
)

enum class DefiStatus {
    WEEKLY,
    DID,
    NO,
}