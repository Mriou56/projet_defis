package ca.uqac.friendschallenge.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.uqac.friendschallenge.components.friend.FriendRankingSection
import ca.uqac.friendschallenge.components.friend.FriendRequestItem
import ca.uqac.friendschallenge.components.friend.FriendSearchBar
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.viewmodel.FriendScreenViewModel


/**
 * The FriendScreen composable function displays the friend screen of the application.
 * It includes friend search, friend requests handling, friend ranking, and navigation to the previous week's score board.
 *
 * It uses a FriendScreenViewModel to manage UI states like search query, friends list, and friend requests.
 *
 * @param modifier The modifier to be applied to the root layout.
 * @param currentUser The current user of the application.
 * @param onWeekButtonClicked Callback function to be invoked when the week button is clicked.
 */
@Composable
fun FriendScreen(
    modifier: Modifier = Modifier,
    currentUser: UserModel,
    onWeekButtonClicked: () -> Unit = {}
) {
    val viewModel: FriendScreenViewModel = viewModel()
    val searchQuery by viewModel.searchQuery
    val searchResults by viewModel.searchResults
    val friends by viewModel.friends
    val friendRequests by viewModel.friendRequests
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search bar for searching friends
        FriendSearchBar(
            searchQuery = searchQuery,
            isLoading = isLoading,
            searchResults = searchResults,
            friends = friends,
            errorMessage = errorMessage,
            currentUser = currentUser,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it, currentUser) },
            onAddFriend = { user -> viewModel.sendFriendRequest(user, currentUser) }
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // List of incoming friend requests
            LazyColumn {
                items(friendRequests) { friendRequest ->
                    FriendRequestItem(
                        userName = friendRequest.friendName,
                        onAccept = { viewModel.acceptFriendRequest(friendRequest, currentUser) },
                        onDecline = { viewModel.rejectFriendRequest(friendRequest, currentUser) }
                    )
                }
            }

            // Ranking section displaying the user's friends
            FriendRankingSection(friends = friends)

            Spacer(modifier = Modifier.weight(1f))

            // Button to navigate to the previous week's score board
            Button(
                onClick = onWeekButtonClicked,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Semaine précédente")
            }
        }
    }
}


@Preview
@Composable
fun PreviewFriends(){
    FriendScreen(
        currentUser = UserModel("uid", "username", "email", 0.0, 0.0),
    )
}