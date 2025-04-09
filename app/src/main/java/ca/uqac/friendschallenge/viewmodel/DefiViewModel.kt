package ca.uqac.friendschallenge.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import ca.uqac.friendschallenge.model.DefiModel
import ca.uqac.friendschallenge.model.ParticipationModel
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth

class DefiViewModel {
    private val firebaseHelper = FirebaseHelper()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val participationsOfFriends = mutableStateOf<List<ParticipationModel>>(emptyList())

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


    fun uploadImage(bitmap: Bitmap, defi: DefiModel, user: UserModel) {
        firebaseHelper.uploadPhoto(
            bitmap = bitmap,
            defi = defi,
            user = user
        ) { result ->
            result
                .onSuccess { url ->
                    Log.d("DefiViewModel", "Image uploaded successfully: $url")
                }
                .onFailure { exception ->
                    Log.e("DefiViewModel", "Error uploading image: ${exception.message}")
                }
        }
    }

    fun voteForImage(
        rating: Float,
        defiId: String,
        participationId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userVotantId = auth.currentUser?.uid ?: return onError("Utilisateur non connecté")

        firebaseHelper.voteForImage(
            rating,
            userVotantId,
            defiId,
            participationId
        ) { result ->
            result.onSuccess { onSuccess() }
                .onFailure { exception -> onError(exception.localizedMessage ?: "Erreur inconnue") }
        }
    }

    fun fetchParticipationsOfFriends(defiId: String, userId: String) {
        Log.d("DEBUG", "Défi utilisé pour chercher les participations : $defiId")
        Log.d("DEBUG", "UserId utilisé pour chercher les participations : $userId")

        firebaseHelper.getParticipationsOfFriends(defiId, userId) { result ->
            result.onSuccess {
                participationsOfFriends.value = it
            }.onFailure {
                Log.e("DefiViewModel", "Erreur: ${it.message}")
            }
        }
    }
}

