package ca.uqac.friendschallenge.components.friend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme

@Composable
fun FriendSearchResultItem(
    user: UserModel,
    alreadyRequested: Boolean,
    onAddFriend: (UserModel) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = user.username, style = MaterialTheme.typography.bodyLarge)
            Button(
                onClick = { onAddFriend(user) },
                enabled = !alreadyRequested
            ) {
                Text(if (alreadyRequested) "En attente" else "Ajouter")
            }
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
fun FriendSearchResultItemPreview() {
    FriendsChallengeTheme {
        FriendSearchResultItem(
            user = UserModel("1", "username", "email", 0.0, 0.0),
            alreadyRequested = false,
            onAddFriend = {}
        )
    }
}