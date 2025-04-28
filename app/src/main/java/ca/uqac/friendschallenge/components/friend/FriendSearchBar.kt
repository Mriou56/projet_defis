package ca.uqac.friendschallenge.components.friend

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.model.FriendModel
import ca.uqac.friendschallenge.model.FriendStatus
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import com.google.firebase.Timestamp

/**
 * The FriendSearchBar composable function displays a search bar for searching friends in the application
 * with their username.
 *
 * @param searchQuery The current search query entered by the user.
 * @param isLoading Indicates whether the search results are being loaded.
 * @param searchResults The list of search results to be displayed.
 * @param friends The list of friends of the current user.
 * @param errorMessage An optional error message to be displayed if an error occurs.
 * @param currentUser The current user of the application.
 * @param onSearchQueryChange Callback function to be invoked when the search query changes.
 * @param onAddFriend Callback function to be invoked when a friend is added.
 */
@Composable
fun FriendSearchBar(
    searchQuery: String,
    isLoading: Boolean,
    searchResults: List<UserModel>,
    friends: List<FriendModel>,
    errorMessage: String?,
    currentUser: UserModel,
    onSearchQueryChange: (String) -> Unit,
    onAddFriend: (UserModel) -> Unit
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = {},
        active = searchQuery.isNotEmpty(),
        onActiveChange = {},
        placeholder = { Text("Rechercher un ami") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon),
                modifier = Modifier.size(32.dp)
            )
        },
        modifier = Modifier
            .padding(horizontal = if (searchQuery.isNotEmpty()) 0.dp else 16.dp)
            .fillMaxWidth(),
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(top = 0.dp)
    ) {
        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.padding(top=32.dp))
                errorMessage != null -> ErrorText(message = errorMessage)
                searchResults.isEmpty() -> EmptyResultsText()
                else -> SearchResultsList(
                    searchResults = searchResults,
                    currentUser = currentUser,
                    friends = friends,
                    onAddFriend = onAddFriend
                )
            }
        }
    }
}

/**
 * Preview function for the FriendSearchBar composable.
 */
@Preview(showBackground = true)
@Composable
fun FriendSearchBarPreview() {
    FriendsChallengeTheme {
        FriendSearchBar(
            searchQuery = "username",
            isLoading = false,
            searchResults = listOf(
                UserModel("2", "username2", "email", 10.0, 1.0),
                UserModel("3", "username3", "email", 20.0, 2.0),
            ),
            friends = listOf(
                FriendModel("2", "username2", FriendStatus.PENDING, "1", Timestamp.now(), 100.0, 10.0),
            ),
            errorMessage = null,
            currentUser = UserModel("1", "username", "email", 100.0, 10.0),
            onSearchQueryChange = {},
            onAddFriend = {}
        )
    }
}
