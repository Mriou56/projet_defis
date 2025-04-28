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

/**
 * The HomeScreen composable function displays the home screen of the application during the week.
 * It shows the weekly challenge and allows the user to participate by taking a picture.
 *
 * @param modifier The modifier to be applied to the root layout.
 * @param userModel The user model representing the current user.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userModel: UserModel
) {
    // Initialize ViewModel and observe its states
    val viewModel: ChallengeViewModel = viewModel()
    val weeklyDefi by viewModel.weeklyChallenge
    val isParticipating by viewModel.isParticipating
    val urlImage by viewModel.participationImageUrl
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Check for camera permission when the composable is first launched
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Display error message as a toast when an error occurs
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastUtils.show(context, errorMessage!!)
        }
    }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher to take a picture using the device camera
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { result: Bitmap? ->
        bitmap = result
    }

    // Launcher to request camera permission if not already granted
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
                .padding(bottom = 72.dp, top = 32.dp)
                ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section displaying the challenge title or loading/error messages
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
            // Section displaying the captured image or challenge image
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
                                // Display image from URL if the user already participated
                                AsyncImage(
                                    model = urlImage,
                                    contentDescription = "Mon image",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                // Display the captured image
                                Image(
                                    bitmap = bitmap!!.asImageBitmap(),
                                    contentDescription = "Captured photo",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            // Floating button to remove the selected image
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
                    // Add some vertical space if no image is available
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }

                // Buttons for participating or validating the challenge
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
                    // Button to upload the challenge photo
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

/**
 * A button to add a challenge.
 *
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 */
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

/**
 * A preview of the HomeScreen composable function.
 *
 * @Preview
 * @Composable
 */
@Composable
@Preview
fun HomeScreenPreview() {
//    FriendsChallengeTheme {
//        HomeScreen()
//    }
}