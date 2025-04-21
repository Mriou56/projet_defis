package ca.uqac.friendschallenge.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.uqac.friendschallenge.model.FriendModel
import ca.uqac.friendschallenge.model.FriendStatus
import ca.uqac.friendschallenge.model.PlayerScore
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.ui.theme.inversePrimaryLight
import ca.uqac.friendschallenge.viewmodel.FriendScreenViewModel
import com.google.firebase.Timestamp


@Composable
fun LeaderboardScreen(currentUser: UserModel, modifier: Modifier = Modifier) {
    val viewModel: FriendScreenViewModel = viewModel()
    val friends by viewModel.friends

    val allPlayers = (friends.map {
        PlayerScore(name = it.friendName, scoreSemaine = it.scoreSemaine)
    } + PlayerScore(name = currentUser.username, scoreSemaine = currentUser.scoreSemaine))
        .sortedByDescending { it.scoreSemaine }
    val top3 = allPlayers.take(3)
    val positionUser = allPlayers.indexOfFirst { it.name == currentUser.username } + 1

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Classement de la semaine",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .background(color = inversePrimaryLight, shape = RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                top3.getOrNull(1)?.let { second ->
                    LeaderboardPodiumPlayer("🥈", second)
                }
                Spacer(modifier = Modifier.width(8.dp))
                top3.getOrNull(0)?.let { first ->
                    LeaderboardPodiumPlayer("🥇", first, isFirst = true)
                }
                Spacer(modifier = Modifier.width(8.dp))
                top3.getOrNull(2)?.let { third ->
                    LeaderboardPodiumPlayer("🥉", third)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(color = inversePrimaryLight, shape = RoundedCornerShape(8.dp))
        ) {
            allPlayers.drop(3).forEachIndexed { index, player ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${index + 4}. ${player.name}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "${player.scoreSemaine.toInt()} pts", style = MaterialTheme.typography.bodyLarge)
                }
                Divider()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Votre position: ${positionUser}ème - ${currentUser.scoreSemaine.toInt()} pts",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
                .background(
                    color = inversePrimaryLight,
                    shape = RoundedCornerShape(8.dp))
        )
    }
}

@Composable
fun LeaderboardPodiumPlayer(medal: String, player: PlayerScore, isFirst: Boolean = false) {
    val boxHeight = if (isFirst) 120.dp else 90.dp
    val bgColor = when (medal) {
        "🥇" -> Color(0xFFFFD700)
        "🥈" -> Color(0xFFD7A9E3)
        "🥉" -> Color(0xFF89CFF0)
        else -> Color.LightGray
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$medal ${player.name}", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        Box(
            modifier = Modifier
                .size(80.dp, boxHeight)
                .background(bgColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${player.scoreSemaine.toInt()} pts", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LeaderboardScreenPreview() {
    val currentUser = UserModel("12", "Margot", "mail@mail", 90.0, 90.0)
    val friends = listOf(
        FriendModel("Alice", "Alice", FriendStatus.ACCEPTED, "Margot", Timestamp.now(), 100.0, 100.0),
        FriendModel("Bob", "Bob", FriendStatus.ACCEPTED, "Margot", Timestamp.now(), 90.0, 90.0),
        FriendModel("Charlie", "Charlie", FriendStatus.ACCEPTED, "Margot", Timestamp.now(), 80.0, 80.0),
        FriendModel("David", "David", FriendStatus.ACCEPTED, "Margot", Timestamp.now(), 70.0, 70.0),
        FriendModel("Eve", "Eve", FriendStatus.ACCEPTED, "Margot", Timestamp.now(), 60.0, 60.0),
    )
    LeaderboardScreen(currentUser = currentUser)
}
