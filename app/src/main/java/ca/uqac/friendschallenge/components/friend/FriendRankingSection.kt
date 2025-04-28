package ca.uqac.friendschallenge.components.friend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.model.FriendModel
import ca.uqac.friendschallenge.model.FriendStatus
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import com.google.firebase.Timestamp

/**
 * The FriendRankingSection composable function displays the ranking of the current user friends
 * in the application.
 *
 * @param friends The list of friends of the current user to be displayed in the ranking section.
 */
@Composable
fun FriendRankingSection(friends: List<FriendModel>) {
    Column {
        Text(
            text = "Classement général - Amis",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )

        // We only filter accepted friends, we sort them by decreasing score
        friends.filter { it.status == FriendStatus.ACCEPTED }
            .sortedByDescending { it.totalScore }
            .forEachIndexed { index, friend ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(friend.friendName, style = MaterialTheme.typography.bodyLarge)
                    Text("${friend.totalScore} pts", style = MaterialTheme.typography.bodyLarge)
                }
                HorizontalDivider()
            }
    }
}

/**
 * Preview function for the FriendRankingSection composable.
 */
@Preview(showBackground = true)
@Composable
fun FriendRankingSectionPreview() {
    val friends = listOf(
        FriendModel("1", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 100.0, 10.0),
        FriendModel("2", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 90.0, 9.0),
        FriendModel("3", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 80.0, 8.0),
        FriendModel("4", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 70.0, 7.0),
        FriendModel("5", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 60.0, 6.0),
        FriendModel("6", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 50.0, 5.0),
        FriendModel("7", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 40.0, 4.0),
        FriendModel("8", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 30.0, 3.0),
        FriendModel("9", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 20.0, 2.0),
        FriendModel("10", "username", FriendStatus.ACCEPTED, "1", Timestamp.now(), 10.0, 1.0),
    )
    FriendsChallengeTheme {
        FriendRankingSection(friends = friends)
    }
}