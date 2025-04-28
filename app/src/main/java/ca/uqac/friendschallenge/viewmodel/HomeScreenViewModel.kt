package ca.uqac.friendschallenge.viewmodel

import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.utils.FirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel class for managing the home screen state.
 *
 * @property firebaseHelper An instance of FirebaseHelper to interact with Firebase services.
 */
class HomeScreenViewModel: ViewModel() {

    private val firebaseHelper = FirebaseHelper()

    private val delay = 5 * 60 * 1000 // 5 minutes
    private var lastCheckedTime = System.currentTimeMillis() - delay
    private var lastIsTimeToVote = false

    private val _isTimeToVote = MutableStateFlow(false)
    val isTimeToVote: StateFlow<Boolean> get() = _isTimeToVote

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        checkIfTimeToVote()
    }

    /**
     * Checks if it's time to vote by comparing the current time with the last checked time.
     * If the delay has passed, it fetches the voting status from Firebase.
     */
    fun checkIfTimeToVote() {
        if (System.currentTimeMillis() - lastCheckedTime < delay) {
            _isTimeToVote.value = lastIsTimeToVote
            _isLoading.value = false
        } else {
            _isLoading.value = true
            lastCheckedTime = System.currentTimeMillis()
            firebaseHelper.isTimeToVote {
                _isTimeToVote.value = it
                lastIsTimeToVote = it
                _isLoading.value = false
            }
        }

    }
}