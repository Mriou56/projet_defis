package ca.uqac.friendschallenge.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.viewmodel.DefiViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: DefiViewModel = remember { DefiViewModel() },
    userModel: UserModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var hasCameraPermission by remember { mutableStateOf(false) }
    val weeklyDefi = viewModel.weeklyDefi
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.fetchWeeklyDefi()
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

//    LaunchedEffect(bitmap) {

//    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp, top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


//            Text(
//                text = stringResource(R.string.week_defi),
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
//            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
//                    Text(
//                        text = stringResource(R.string.consigne),
//                        style = MaterialTheme.typography.headlineSmall,
//                        modifier = Modifier.padding(bottom = 4.dp)
//                    )
                    when {
                        isLoading -> Text("Chargement...")
                        errorMessage != null -> Text("Erreur : $errorMessage")
                        weeklyDefi != null -> Text(
                            text = weeklyDefi.consigne,
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center
                        )
                        else -> Text("Aucun défi trouvé.")
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                if (bitmap != null) {
                    Column(modifier = Modifier.padding(bottom = 30.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp)
                        ) {
                            Image(
                                bitmap = bitmap!!.asImageBitmap(),
                                contentDescription = "Captured photo",
                                modifier = Modifier.fillMaxSize()
                            )

                            SmallFloatingActionButton(
                                onClick = {
                                    bitmap = null
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove photo",
                                )
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(150.dp))
                }

                if (bitmap == null) {
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    )
                } else {
                    Button(
                        onClick = {
                            if (weeklyDefi != null && bitmap != null) {
                                viewModel.uploadImage(bitmap!!, weeklyDefi, userModel)
                                bitmap = null // Reset bitmap after upload
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    ) {
                        Text(
                            text = "Valider le défi",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }


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
    ) {
        Text(
            text = "Participer",
            style = MaterialTheme.typography.labelLarge
        )
    }
}


@Composable
@Preview
fun HomeScreenPreview() {
//    FriendsChallengeTheme {
//        HomeScreen()
//    }
}