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

@Preview(showBackground = true)
@Composable
fun FriendSearchBarPreview() {
    FriendsChallengeTheme {
        FriendSearchBar(
            searchQuery = "username",
            isLoading = false,
            searchResults = listOf(
                UserModel("2", "username2", "email"),
                UserModel("3", "username3", "email"),
            ),
            friends = listOf(
                FriendModel("2", "username2", FriendStatus.PENDING, "1", Timestamp.now()),
            ),
            errorMessage = null,
            currentUser = UserModel("1", "username", "email"),
            onSearchQueryChange = {},
            onAddFriend = {}
        )
    }
}
