package ca.uqac.friendschallenge.components.friend

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Error message of the application
 */
@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(32.dp)
    )
}

/**
 * Empty results message of the application
 */
@Composable
fun EmptyResultsText() {
    Text(
        text = "Aucun résultat",
        modifier = Modifier.padding(32.dp)
    )
}

@Composable
@Preview
fun ErrorTextPreview() {
    ErrorText("Erreur de connexion")
}

@Composable
@Preview
fun EmptyResultsTextPreview() {
    EmptyResultsText()
}

