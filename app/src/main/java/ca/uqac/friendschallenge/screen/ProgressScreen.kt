package ca.uqac.friendschallenge.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme

/**
 * The ProgressScreen composable function displays a loading screen with a circular progress indicator.
 *
 * @param modifier The modifier to be applied to the root layout.
 */
@Composable
fun ProgressScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        // Centered circular loading indicator
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.inversePrimary,
            strokeWidth = 8.dp,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    FriendsChallengeTheme {
        ProgressScreen()
    }
}