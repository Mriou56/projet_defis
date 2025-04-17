package ca.uqac.friendschallenge.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.model.Challenge
import ca.uqac.friendschallenge.model.ChallengeStatus
import ca.uqac.friendschallenge.model.ParticipationModel
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.FirebaseHelper

class ChallengeViewModel : ViewModel(){
    private val firebaseHelper = FirebaseHelper()
    val participationsOfFriends = mutableStateOf<List<ParticipationModel>>(emptyList())

    // Get the weekly defi and show it in the UI
    private val _weeklyChallenge = mutableStateOf<Challenge?>(null)
    val weeklyChallenge: State<Challenge?> get() = _weeklyChallenge

    private val _isParticipating = mutableStateOf(false)
    val isParticipating: State<Boolean> get() = _isParticipating

    private val _participationImageUrl = mutableStateOf<String?>(null)
    val participationImageUrl: State<String?> get() = _participationImageUrl

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    init {
        fetchWeeklyChallenge()
    }


    fun fetchWeeklyChallenge() {
        _isLoading.value = true
        firebaseHelper.getWeeklyChallenge { result ->
            _isLoading.value = false
            result
                .onSuccess { challenge ->
                    _weeklyChallenge.value = challenge
                    checkIfParticipating(challenge.id)
                    _errorMessage.value = null
                }
                .onFailure { exception ->
                    _weeklyChallenge.value = null
                    _errorMessage.value = exception.message
                }
        }
    }

    fun checkIfParticipating(challengeId: String) {
        _isLoading.value = true
        firebaseHelper.fetchUserImages { result ->
            _isLoading.value = false
            result
                .onSuccess { participations ->
                    val participation = participations.find { it.id == challengeId }
                    _isParticipating.value = participation != null
                    _participationImageUrl.value = participation?.imageUrl
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.localizedMessage
                }
        }
    }


    fun participatingToChallenge(bitmap: Bitmap, challenge: Challenge, user: UserModel) {
        firebaseHelper.submitChallengeParticipation(
            bitmap = bitmap,
            challenge = challenge,
            user = user
        ) { result ->
            result
                .onSuccess { url ->
                    _isParticipating.value = true
                    _participationImageUrl.value = url
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.localizedMessage
                }
        }
    }

    fun deleteParticipation(challengeId: String) {
        _isLoading.value = true
        firebaseHelper.deleteParticipation(challengeId) { result ->
            _isLoading.value = false
            result
                .onSuccess {
                    _isParticipating.value = false
                    _participationImageUrl.value = null
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.localizedMessage
                }
        }
    }

    fun voteForImage(
        rating: Float,
        challengeId: String,
        participationId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseHelper.voteForImage(
            rating,
            challengeId,
            participationId
        ) { result ->
            result.onSuccess { onSuccess() }
                .onFailure { exception -> onError(exception.localizedMessage ?: "Erreur inconnue") }
        }
    }

    fun fetchParticipationsOfFriends(challengeId: String, userId: String) {
        firebaseHelper.getParticipationsOfFriends(challengeId, userId) { result ->
            result.onSuccess {
                participationsOfFriends.value = it
            }.onFailure {
                _errorMessage.value = it.localizedMessage
            }
        }
    }
}

