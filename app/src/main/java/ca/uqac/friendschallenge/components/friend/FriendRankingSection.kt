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

        friends.filter { it.status == FriendStatus.ACCEPTED }
            .forEachIndexed { index, friend ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(friend.friendName, style = MaterialTheme.typography.bodyLarge)
                    Text("${100 - index * 5} pts", style = MaterialTheme.typography.bodyLarge)
                }
                HorizontalDivider()
            }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendRankingSectionPreview() {
    val friends = listOf(
        FriendModel("1", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("2", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("3", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("4", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("5", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("6", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("7", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("8", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("9", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
        FriendModel("10", "username", FriendStatus.ACCEPTED, "1", Timestamp.now()),
    )
    FriendsChallengeTheme {
        FriendRankingSection(friends = friends)
    }
}