package ca.uqac.friendschallenge.viewmodel

import androidx.compose.runtime.mutableStateOf
import ca.uqac.friendschallenge.model.DefiModel
import ca.uqac.friendschallenge.utils.FirebaseHelper

class DefiViewModel {
    private val firebaseHelper = FirebaseHelper()

    // Get the weekly defi and show it in the UI
    private val _weeklyDefi = mutableStateOf<DefiModel?>(null)
    val weeklyDefi: DefiModel?
        get() = _weeklyDefi.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading.value

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: String?
        get() = _errorMessage.value


    fun fetchWeeklyDefi() {
        _isLoading.value = true
        firebaseHelper.getWeeklyDefi { result ->
            _isLoading.value = false
            result
                .onSuccess { (id, consigne) ->
                    _weeklyDefi.value = DefiModel(
                        id = id,
                        consigne = consigne,
                        realise = "WEEKLY"
                    )
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _weeklyDefi.value = null
                    _errorMessage.value = exception.message
                }
        }
    }

}