package ca.uqac.friendschallenge.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.model.ChallengeModel
import ca.uqac.friendschallenge.model.ParticipationModel
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.FirebaseHelper

/**
 * ViewModel for managing the challenge-related data and operations.
 *
 * @property firebaseHelper An instance of FirebaseHelper to interact with Firebase services.
 */
class ChallengeViewModel : ViewModel() {
    private val firebaseHelper = FirebaseHelper()
    val participationsOfFriends = mutableStateOf<List<ParticipationModel>>(emptyList())

    // Get the weekly defi and show it in the UI
    private val _weeklyChallenge = mutableStateOf<ChallengeModel?>(null)
    val weeklyChallenge: State<ChallengeModel?> get() = _weeklyChallenge

    private val _isParticipating = mutableStateOf(false)
    val isParticipating: State<Boolean> get() = _isParticipating

    private val _participationImageUrl = mutableStateOf<String?>(null)
    val participationImageUrl: State<String?> get() = _participationImageUrl

    private val _hasVoted = mutableStateOf<Boolean?>(null)
    val hasVoted: State<Boolean?> = _hasVoted

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    init {
        fetchWeeklyChallenge()
    }


    /**
     * Fetches the weekly challenge from Firebase and updates the UI state.
     */
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

    /**
     * Checks if the user is participating in the challenge.
     *
     * @param challengeId The ID of the challenge to check participation for.
     */
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

    /**
     * Submits a participation for the challenge.
     *
     * @param bitmap The bitmap image to submit.
     * @param challenge The challenge to participate in.
     * @param user The user participating in the challenge.
     */
    fun participatingToChallenge(bitmap: Bitmap, challenge: ChallengeModel, user: UserModel) {
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

    /**
     * Deletes the participation for the challenge.
     *
     * @param challengeId The ID of the challenge to delete participation for.
     */
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

    /**
     * Votes for an image in the challenge.
     *
     * @param rating The rating to give to the image.
     * @param challengeId The ID of the challenge.
     * @param participationId The ID of the participation.
     * @param onSuccess Callback function to be called on success.
     * @param onError Callback function to be called on error.
     */
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

    /**
     * Fetches the participations of friends for the challenge.
     *
     * @param challengeId The ID of the challenge.
     * @param userId The ID of the user.
     */
    fun fetchParticipationsOfFriends(challengeId: String, userId: String) {
        firebaseHelper.getParticipationsOfFriends(challengeId, userId) { result ->
            result.onSuccess {
                participationsOfFriends.value = it
            }.onFailure {
                _errorMessage.value = it.localizedMessage
            }
        }
    }

    /**
     * Checks if the user has already voted for the challenge.
     *
     * @param userId The ID of the user.
     */
    fun checkIfUserVoted(userId: String) {
        _hasVoted.value = null
        firebaseHelper.checkIfUserVoted(userId) { voted ->
            _hasVoted.value = voted
        }
    }

    /**
     * Marks the voting as completed for the user.
     *
     * @param userId The ID of the user.
     */
    fun markVotingCompleted(userId: String) {
        firebaseHelper.markVotingCompleted(userId) { result ->
            result.onSuccess {
                _hasVoted.value = true
            }.onFailure {
                _errorMessage.value = it.localizedMessage
            }
        }
    }
}

