package ca.uqac.friendschallenge.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.ToastUtils
import ca.uqac.friendschallenge.viewmodel.ChallengeViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userModel: UserModel
) {
    val viewModel: ChallengeViewModel = viewModel()
    val weeklyDefi by viewModel.weeklyChallenge
    val isParticipating by viewModel.isParticipating
    val urlImage by viewModel.participationImageUrl
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var hasCameraPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastUtils.show(context, errorMessage!!)
        }
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
                    when {
                        isLoading -> Text("Chargement...")
                        errorMessage != null -> Text("Erreur : $errorMessage")
                        weeklyDefi != null -> Text(
                            text = weeklyDefi!!.title,
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
                                onClick = {
                                    bitmap = null
                                    viewModel.deleteParticipation(weeklyDefi!!.id)
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
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }

                if (bitmap == null && !isParticipating) {
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
                } else if (!isParticipating) {
                    Button(
                        onClick = {
                            if (weeklyDefi != null && bitmap != null) {
                                viewModel.participatingToChallenge(bitmap!!, weeklyDefi!!, userModel)
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