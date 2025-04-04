package ca.uqac.friendschallenge.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.ui.theme.backgroundLight
import ca.uqac.friendschallenge.ui.theme.inversePrimaryLight
import ca.uqac.friendschallenge.ui.theme.onPrimaryLight
import ca.uqac.friendschallenge.ui.theme.primaryContainerLight
import ca.uqac.friendschallenge.ui.theme.primaryLight
import ca.uqac.friendschallenge.viewmodel.DefiViewModel

@Composable
fun VoteScreen(modifier: Modifier = Modifier, viewModel: DefiViewModel = remember { DefiViewModel() }) {
    var rating by remember { mutableStateOf(5f) }
    val weeklyDefi = viewModel.weeklyDefi
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.fetchWeeklyDefi()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            isLoading ->
                Text("Chargement...",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .background(
                            color = inversePrimaryLight,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp))
            errorMessage != null -> Text("Erreur : $errorMessage")
            weeklyDefi != null -> Text(
                text = weeklyDefi.consigne,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .background(
                        color = inversePrimaryLight,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            )
            else -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        color = inversePrimaryLight,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(40.dp)
            )
        }

        Text(
            text = "Paul",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start)
                .padding(start = 16.dp)
        )

        Image(
            painter = painterResource(R.drawable.erable),
            contentDescription = "Photo d'un érable",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
                colors = androidx.compose.material3.SliderDefaults.colors(
                    thumbColor = primaryLight,
                    activeTrackColor = primaryLight,
                    inactiveTrackColor = primaryContainerLight
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = { /* Logique pour valider la note */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryLight,
                contentColor = onPrimaryLight
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Valider")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview(showBackground = true)
fun VoteScreenPreview() {
    VoteScreen()
}