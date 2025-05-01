package ca.uqac.friendschallenge.components.home

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChallengeActionButtons(
    bitmap: Bitmap?,
    isParticipating: Boolean,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit,
    onTakePicture: () -> Unit,
    onValidate: () -> Unit
) {
    if (bitmap == null && !isParticipating) {
        AddChallengeButton(
            onClick = {
                if (hasCameraPermission) onTakePicture() else onRequestPermission()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
    } else if (!isParticipating) {
        Button(
            onClick = onValidate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Valider le défi", style = MaterialTheme.typography.labelLarge)
        }
    }
}
