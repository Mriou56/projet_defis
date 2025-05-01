package ca.uqac.friendschallenge.components.home

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ChallengeImageSection(
    bitmap: Bitmap?,
    urlImage: String?,
    isParticipating: Boolean,
    onRemoveImage: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (bitmap != null || (urlImage != null && isParticipating)) {
            Column(modifier = Modifier.padding(bottom = 30.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) {
                    if (urlImage != null && isParticipating) {
                        AsyncImage(
                            model = urlImage,
                            contentDescription = "Mon image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = "Captured photo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    SmallFloatingActionButton(
                        onClick = onRemoveImage,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove photo")
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.fillMaxHeight(0.5f))
        }
    }
}
