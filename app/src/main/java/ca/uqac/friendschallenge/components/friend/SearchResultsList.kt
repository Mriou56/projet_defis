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

@Composable
fun SearchResultsList(
    searchResults: List<UserModel>,
    currentUser: UserModel,
    friends: List<FriendModel>,
    onAddFriend: (UserModel) -> Unit
) {
    LazyColumn {
        items(searchResults) { user ->
            if (shouldDisplayUser(user, currentUser, friends)) {
                val alreadyRequested = isFriendRequestPending(user, friends)
                FriendSearchResultItem(user, alreadyRequested, onAddFriend)
            }
        }
    }
}

private fun shouldDisplayUser(user: UserModel, currentUser: UserModel, friends: List<FriendModel>): Boolean {
    return user.uid != currentUser.uid && !friends.any { it.friendId == user.uid && it.status == FriendStatus.ACCEPTED }
}

private fun isFriendRequestPending(user: UserModel, friends: List<FriendModel>): Boolean {
    return friends.any { it.friendId == user.uid && it.status == FriendStatus.PENDING }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchResultsList() {
    FriendsChallengeTheme {
        SearchResultsList(
            searchResults = listOf(
                UserModel(uid = "1", username = "Alice", email = "alice@alice.com", scoreSemaine = 100.0, totalScore = 1000.0),
                UserModel(uid = "2", username = "Bob", email = "bob@bob.com", scoreSemaine = 100.0, totalScore = 200.0),
            ),
            currentUser = UserModel(uid = "0", username = "CurrentUser", email = "currentuser@current.com", scoreSemaine = 100.0, totalScore = 1000.0),
            friends = listOf(
                FriendModel(friendId = "1", friendName = "Alice", status = FriendStatus.PENDING, sentBy = "1", createdAt = Timestamp.now(), totalScore = 100.0, scoreSemaine = 10.0),
            ),
            onAddFriend = {}
        )
    }
}
