package ca.uqac.friendschallenge.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.model.ChallengeModel

@Composable
fun ChallengeHeaderSection(
    isLoading: Boolean,
    errorMessage: String?,
    weeklyDefi: ChallengeModel?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when {
                isLoading -> Text("Chargement...")
                errorMessage != null -> Text("Erreur : $errorMessage")
                weeklyDefi != null -> Text(
                    text = weeklyDefi.title,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )

                else -> Text("Aucun défi trouvé.")
            }
        }
    }
}
