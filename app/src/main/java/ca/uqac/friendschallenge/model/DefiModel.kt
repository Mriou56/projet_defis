package ca.uqac.friendschallenge.model

import androidx.compose.runtime.State

data class DefiModel (
    val id: String,
    val consigne: String,
    val realise: Boolean,
)

enum class DefiStatus {
    WEEK,
    DID,
    NOT_DID,
}