package ca.uqac.friendschallenge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.MainBottomBar
import ca.uqac.friendschallenge.MainScreen
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.ui.theme.inversePrimaryLight
import ca.uqac.friendschallenge.ui.theme.onPrimaryLight
import ca.uqac.friendschallenge.ui.theme.primaryContainerLight
import ca.uqac.friendschallenge.ui.theme.primaryLight

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

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
            text = stringResource(R.string.week_defi),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.consigne),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus molestie sed quam sit amet euismod. Aliquam nisl dolor, iaculis et egestas ut, ultrices quis quam.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(150.dp))

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth())
        {
            addChallengeButton(
                onClick = {},
            )
        }

        Spacer(modifier = Modifier.height(250.dp))

        MainBottomBar(
            currentScreen = MainScreen.Home,
            onHomeButtonClicked = {},
            onFriendsButtonClicked = {},
            onProfileButtonClicked = {}
        )
    }
}

@Composable
fun addChallengeButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryLight,
            contentColor = onPrimaryLight
        ),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Ajouter le résulat du défi",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}