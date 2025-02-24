package ca.uqac.friendschallenge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.MainBottomBar
import ca.uqac.friendschallenge.MainScreen
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.ui.theme.inversePrimaryLight
import ca.uqac.friendschallenge.ui.theme.primaryContainerLight

@Composable
fun LeaderboardScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(primaryContainerLight)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(inversePrimaryLight)
        ) {
            Image(
                painter = painterResource(R.drawable.logo1),
                contentDescription = "Logo of the app, two hands shaking below a cup",
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 8.dp)
            )
        }

        Text(
            text = "Classement de la semaine",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).wrapContentWidth(Alignment.CenterHorizontally)
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🥈 Joueur 2", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                    Box(
                        modifier = Modifier
                            .size(80.dp, 90.dp)
                            .background(Color(0xFFD7A9E3), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "90 pts", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🥇 Joueur 1", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                    Box(
                        modifier = Modifier
                            .size(80.dp, 120.dp)
                            .background(Color(0xFFFFD700), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "100 pts", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🥉 Joueur 3", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                    Box(
                        modifier = Modifier
                            .size(80.dp, 90.dp)
                            .background(Color(0xFF89CFF0), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "80 pts", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            repeat(5) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Joueur ${index + 4}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "${(70 - index * 10)} pts", style = MaterialTheme.typography.bodyLarge)
                }
                Divider()
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Votre position: 7ème - 60 pts",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally).padding(bottom = 8.dp)
        )
        MainBottomBar(
            currentScreen = MainScreen.Home,
            onHomeButtonClicked = {},
            onFriendsButtonClicked = {},
            onProfileButtonClicked = {}
        )
    }
}

@Composable
@Preview
fun LeaderboardScreenPreview() {
    LeaderboardScreen()
}