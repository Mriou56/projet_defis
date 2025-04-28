package ca.uqac.friendschallenge.components.friend

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ca.uqac.friendschallenge.model.FriendModel
import ca.uqac.friendschallenge.model.FriendStatus
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import com.google.firebase.Timestamp

/**
 * The SearchResultsList composable function displays a list of search results for friends in the application.
 *
 * @param searchResults The list of search results to be displayed.
 * @param currentUser The current user of the application.
 * @param friends The list of friends of the current user.
 * @param onAddFriend Callback function to be invoked when a friend is added.
 */
@Composable
fun SearchResultsList(
    searchResults: List<UserModel>,
    currentUser: UserModel,
    friends: List<FriendModel>,
    onAddFriend: (UserModel) -> Unit
) {
    // LazyColumn to display the list of search results
    LazyColumn {
        items(searchResults) { user ->
            if (shouldDisplayUser(user, currentUser, friends)) {
                val alreadyRequested = isFriendRequestPending(user, friends)
                FriendSearchResultItem(user, alreadyRequested, onAddFriend)
            }
        }
    }
}

/**
 * Determines whether a user should be displayed in the search results list based on the current user
 * and their friends.
 *
 * @param user The user model to check.
 * @param currentUser The current user of the application.
 * @param friends The list of friends of the current user.
 * @return True if the user should be displayed, false otherwise.
 */
private fun shouldDisplayUser(user: UserModel, currentUser: UserModel, friends: List<FriendModel>): Boolean {
    return user.uid != currentUser.uid && !friends.any { it.friendId == user.uid && it.status == FriendStatus.ACCEPTED }
}

/**
 * Checks if a friend request is pending for the given user.
 *
 * @param user The user model to check.
 * @param friends The list of friends of the current user.
 * @return True if a friend request is pending, false otherwise.
 */
private fun isFriendRequestPending(user: UserModel, friends: List<FriendModel>): Boolean {
    return friends.any { it.friendId == user.uid && it.status == FriendStatus.PENDING }
}

/**
 * Preview function for the SearchResultsList composable.
 */
@Preview(showBackground = true)
@Composable
fun PreviewSearchResultsList() {
    FriendsChallengeTheme {
        SearchResultsList(
            searchResults = listOf(
                UserModel(uid = "1", username = "Alice", email = "alice@alice.com", scoreWeek = 100.0, totalScore = 1000.0),
                UserModel(uid = "2", username = "Bob", email = "bob@bob.com", scoreWeek = 100.0, totalScore = 200.0),
            ),
            currentUser = UserModel(uid = "0", username = "CurrentUser", email = "currentuser@current.com", scoreWeek = 100.0, totalScore = 1000.0),
            friends = listOf(
                FriendModel(friendId = "1", friendName = "Alice", status = FriendStatus.PENDING, sentBy = "1", createdAt = Timestamp.now(), totalScore = 100.0, scoreWeek = 10.0),
            ),
            onAddFriend = {}
        )
    }
}
