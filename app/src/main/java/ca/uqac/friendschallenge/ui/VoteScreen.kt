package ca.uqac.friendschallenge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun VoteScreen(modifier: Modifier = Modifier) {
    var rating by remember { mutableStateOf(5f) }

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
            text = "Prendre une photo d'un érable",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
        )

        Text(
            text = "Paul",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start).padding(start = 16.dp)
        )

        Image(
            painter = painterResource(R.drawable.erable),
            contentDescription = "Photo d'un érable",
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Note du défi : ${rating.toInt()}")
            Slider(
                value = rating,
                onValueChange = { rating = it },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { /* Logique pour valider la note */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Valider")
        }

        Spacer(modifier = Modifier.weight(1f))
        MainBottomBar(
            currentScreen = MainScreen.Friends,
            onHomeButtonClicked = {},
            onFriendsButtonClicked = {},
            onProfileButtonClicked = {}
        )
    }
}

@Composable
@Preview
fun VoteScreenPreview() {
    VoteScreen()
}