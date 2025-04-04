package ca.uqac.friendschallenge.viewmodel

import androidx.compose.runtime.mutableStateOf
import ca.uqac.friendschallenge.utils.FirebaseHelper

class DefiViewModel {
    private val firebaseHelper = FirebaseHelper()

    private val _weeklyDefi = mutableStateOf("")
    val weeklyDefi: String
        get() = _weeklyDefi.value

}