package ca.uqac.friendschallenge.components.friend

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(32.dp)
    )
}

@Composable
fun EmptyResultsText() {
    Text(
        text = "Aucun résultat",
        modifier = Modifier.padding(32.dp)
    )
}

