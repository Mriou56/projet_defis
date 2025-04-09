package ca.uqac.friendschallenge.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.model.ImageModel
import ca.uqac.friendschallenge.utils.FirebaseHelper

class ProfileViewModel : ViewModel()  {
    private val firebaseHelper = FirebaseHelper()

    private val _images = mutableStateOf(emptyList<ImageModel>())
    val images: State<List<ImageModel>> get() = _images

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    fun loadImages(){
        _isLoading.value = true
        _errorMessage.value = null

        firebaseHelper.fetchUserImages { result ->
            _isLoading.value = false
            result.onSuccess { images ->
                _images.value = images
                _errorMessage.value = null
            }
            result.onFailure { exception ->
                _errorMessage.value = exception.localizedMessage
            }
        }
    }
}