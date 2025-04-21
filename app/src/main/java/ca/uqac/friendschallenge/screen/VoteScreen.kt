package ca.uqac.friendschallenge.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.ui.theme.inversePrimaryLight
import ca.uqac.friendschallenge.ui.theme.onPrimaryLight
import ca.uqac.friendschallenge.ui.theme.primaryContainerLight
import ca.uqac.friendschallenge.ui.theme.primaryLight
import ca.uqac.friendschallenge.viewmodel.ChallengeViewModel
import coil.compose.AsyncImage
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.uqac.friendschallenge.utils.ToastUtils

@Composable
fun VoteScreen(modifier: Modifier = Modifier, currentUser: UserModel) {
    val viewModel: ChallengeViewModel = viewModel()
    var rating by remember { mutableStateOf(5f) }
    var currentIndex by remember { mutableStateOf(0) }
    val participations by viewModel.participationsOfFriends
    val challenge by viewModel.weeklyChallenge
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val hasVoted = viewModel.hasVoted.value

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchWeeklyChallenge()
        viewModel.checkIfUserVoted(currentUser.uid)
    }

    LaunchedEffect(challenge) {
        if (challenge != null) {
            viewModel.fetchParticipationsOfFriends(challengeId = challenge!!.id, userId = currentUser.uid)
        }
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastUtils.show(context, errorMessage!!)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            isLoading -> {
                Text("Chargement...", style = MaterialTheme.typography.headlineMedium)
            }
            errorMessage != null -> {
                Text("Erreur : $errorMessage")
            }
            hasVoted == true -> {
                Text(
                    "Merci ! Vous avez évalué toutes les participations de vos amis. 🎉",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "Pensez à revenir plus tard pour voir les résultats !",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            challenge != null && participations.isNotEmpty() -> {
                val participation = participations[currentIndex]

                Text(
                    text = challenge!!.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .background(color = inversePrimaryLight)
                        .padding(8.dp)
                )

                Text(
                    text = "Utilisateur : ${participation.userName}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )

                AsyncImage(
                    model = participation.imageUrl,
                    contentDescription = "Image du défi",
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
                        colors = SliderDefaults.colors(
                            thumbColor = primaryLight,
                            activeTrackColor = primaryLight,
                            inactiveTrackColor = primaryContainerLight
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    onClick = {
                        viewModel.voteForImage(
                            rating,
                            challenge!!.id,
                            participation.id,
                            onSuccess = {
                                if (currentIndex < participations.lastIndex) {
                                    currentIndex++
                                    rating = 5f
                                } else {
                                    Log.d("VoteScreen", "Tous les votes ont été envoyés")
                                    viewModel.markVotingCompleted(currentUser.uid)
                                }
                            },
                            onError = {
                                Log.e("VoteScreen", "Erreur lors du vote : $it")
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryLight,
                        contentColor = onPrimaryLight
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Valider")
                }
            }

            challenge != null -> {
                Text("Aucune participation d’amis à évaluer.")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}