package ca.uqac.friendschallenge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
fun FriendScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }

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

        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(12.dp),
            singleLine = true
        )

        Text(
            text = "Classement général - Amis",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            repeat(5) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Ami ${index + 1}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "${(100 - index * 5)} pts", style = MaterialTheme.typography.bodyLarge)
                }
                Divider()
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {},
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Semaine précédente")
        }

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
fun FriendScreenPreview() {
    FriendScreen()
}
