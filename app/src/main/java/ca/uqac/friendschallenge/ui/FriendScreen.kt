package ca.uqac.friendschallenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import ca.uqac.friendschallenge.ui.theme.primaryContainerLight



@Composable
fun FriendScreen(modifier: Modifier = Modifier,
                 onWeekButtonClicked: () -> Unit = {}, ) {
    var searchQuery by remember { mutableStateOf("Chercher des amis") }

    Column(
        modifier = modifier
            .background(primaryContainerLight)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Bar with Rounded Style
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(4.dp, RoundedCornerShape(50))
                .background(Color.White, RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_icon),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

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
            onClick = {onWeekButtonClicked() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Semaine précédente")
        }
    }
}



@Composable
@Preview
fun FriendScreenPreview() {
    FriendsChallengeTheme {
        FriendScreen(onWeekButtonClicked = {})
    }
}
