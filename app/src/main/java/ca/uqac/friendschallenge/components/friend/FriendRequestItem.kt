package ca.uqac.friendschallenge.components.friend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme

/**
 * The FriendRequestItem composable function displays a single friend request item with the user's name
 * and buttons to accept or decline the request.
 *
 * @param userName The name of the user who sent the friend request.
 * @param onAccept Callback function to be invoked when the accept button is clicked.
 * @param onDecline Callback function to be invoked when the decline button is clicked.
 */
@Composable
fun FriendRequestItem(
    userName: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display the user's name who sent the friend request
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            // Buttons to accept or decline the friend request
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Accepter")
                }
                Button(
                    onClick = onDecline,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Refuser")
                }
            }
        }
        HorizontalDivider()
    }
}

/**
 * Preview function for the FriendRequestItem composable.
 */
@Preview(showBackground = true)
@Composable
fun FriendRequestItemPreview() {
    FriendsChallengeTheme {
        FriendRequestItem(
            userName = "username",
            onAccept = {},
            onDecline = {}
        )
    }
}