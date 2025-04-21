package ca.uqac.friendschallenge.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ca.uqac.friendschallenge.model.FriendModel
import ca.uqac.friendschallenge.model.FriendStatus
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.FirebaseHelper
import com.google.firebase.Timestamp

class FriendScreenViewModel : ViewModel() {

    private val firebaseHelper = FirebaseHelper()

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> get() = _searchQuery

    private val _searchResults = mutableStateOf(emptyList<UserModel>())
    val searchResults: State<List<UserModel>> get() = _searchResults

    private val _friends = mutableStateOf(emptyList<FriendModel>())
    val friends: State<List<FriendModel>> get() = _friends

    private val _friendRequests = mutableStateOf(emptyList<FriendModel>())
    val friendRequests: State<List<FriendModel>> get() = _friendRequests

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    private var lastSearchTime: Long = 0

    init {
        fetchFriends()
    }

    private fun fetchFriends() {
        _isLoading.value = true
        _errorMessage.value = null

        firebaseHelper.fetchUserFriends() { result ->
            _isLoading.value = false
            result.onSuccess { friends ->
                _friends.value = friends
                _friendRequests.value = friends.filter { it.status == FriendStatus.REQUESTED }
            }
            result.onFailure { exception ->
                _errorMessage.value = exception.localizedMessage
            }
        }
    }

    fun onSearchQueryChange(query: String, currentUser: UserModel) {
        _searchQuery.value = query

        // Proceed with the search only if query length is greater than 2 characters
        // and at least 500ms have passed since the last search.
        if (query.length > 2 && System.currentTimeMillis() - lastSearchTime > 500) {
            performSearch(currentUser)
        }

        if (query.isEmpty()) {
            _searchResults.value = emptyList()
        }
    }

    private fun performSearch(currentUser: UserModel) {
        lastSearchTime = System.currentTimeMillis()
        _isLoading.value = true
        _errorMessage.value = null

        firebaseHelper.searchUsersByUsername(_searchQuery.value) { result ->
            _isLoading.value = false
            result.onSuccess { users ->
                // Remove the current user from the search results
                _searchResults.value = users.filter { it.uid != currentUser.uid }
            }
            result.onFailure { exception ->
                _errorMessage.value = exception.localizedMessage
            }
        }
    }

    fun sendFriendRequest(friend: UserModel, currentUser: UserModel) {
        val senderModel = FriendModel(
            friend.uid, friend.username, FriendStatus.PENDING, currentUser.uid, Timestamp.now(), 0.0, 0.0
        )
        val receiverModel = FriendModel(
            currentUser.uid, currentUser.username, FriendStatus.REQUESTED, currentUser.uid, Timestamp.now(), 0.0, 0.0
        )

        firebaseHelper.performFriendOperation(currentUser.uid, friend.uid, senderModel, receiverModel) { result ->
            result.onSuccess {
                fetchFriends()
            }
            result.onFailure {
                _errorMessage.value = it.localizedMessage
            }
        }
    }

    fun acceptFriendRequest(friend: FriendModel, currentUser: UserModel) {
        val senderModel = friend.copy(status = FriendStatus.ACCEPTED, createdAt = Timestamp.now())
        val receiverModel = FriendModel(
            currentUser.uid, currentUser.username, FriendStatus.ACCEPTED, friend.sentBy, Timestamp.now(), 0.0, 0.0
        )

        firebaseHelper.performFriendOperation(currentUser.uid, friend.friendId, senderModel, receiverModel) { result ->
            result.onSuccess {
                fetchFriends()
            }
            result.onFailure {
                _errorMessage.value = it.localizedMessage
            }
        }
    }

    fun rejectFriendRequest(friend: FriendModel, currentUser: UserModel) {
        val senderModel = friend.copy(status = FriendStatus.REJECTED, createdAt = Timestamp.now())
        val receiverModel = FriendModel(
            currentUser.uid, currentUser.username, FriendStatus.REJECTED, friend.sentBy, Timestamp.now(), 0.0, 0.0
        )

        firebaseHelper.performFriendOperation(currentUser.uid, friend.friendId, senderModel, receiverModel) { result ->
            result.onSuccess {
                fetchFriends()
            }
            result.onFailure {
                _errorMessage.value = it.localizedMessage
            }
        }
    }
}