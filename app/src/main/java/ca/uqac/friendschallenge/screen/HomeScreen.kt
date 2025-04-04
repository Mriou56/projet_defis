package ca.uqac.friendschallenge.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.data.allDefi
import ca.uqac.friendschallenge.ui.theme.onPrimaryLight
import ca.uqac.friendschallenge.ui.theme.primaryContainerLight
import ca.uqac.friendschallenge.ui.theme.primaryLight

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.asImageBitmap
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var hasCameraPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { result: Bitmap? ->
        bitmap = result
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean -> hasCameraPermission = isGranted

        if (isGranted) {
            takePicture.launch(null)
        }
    }

    val defi = allDefi.random()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            Text(
                text = stringResource(R.string.week_defi),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.consigne),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = defi,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(150.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                Column(modifier= Modifier.padding(bottom = 30.dp)) {
                    bitmap?.let {
                        Image(bitmap = it.asImageBitmap(), contentDescription = "Captured photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp))
                    }
                }

                AddChallengeButton(
                    onClick = {
                        if (hasCameraPermission) {
                            takePicture.launch(null)
                        } else {
                            scope.launch {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun AddChallengeButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Ajouter le résulat du défi",
            style = MaterialTheme.typography.labelLarge
        )
    }
}


@Composable
@Preview
fun HomeScreenPreview() {
    FriendsChallengeTheme {
        HomeScreen()
    }
}