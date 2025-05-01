package ca.uqac.friendschallenge.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.uqac.friendschallenge.components.home.ChallengeActionButtons
import ca.uqac.friendschallenge.components.home.ChallengeHeaderSection
import ca.uqac.friendschallenge.components.home.ChallengeImageSection
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.utils.ToastUtils
import ca.uqac.friendschallenge.viewmodel.ChallengeViewModel
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
    val viewModel: ChallengeViewModel = viewModel()
    val weeklyDefi by viewModel.weeklyChallenge
    val isParticipating by viewModel.isParticipating
    val urlImage by viewModel.participationImageUrl
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var hasCameraPermission by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

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

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { result: Bitmap? -> bitmap = result }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
        if (isGranted) takePicture.launch(null)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp, top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChallengeHeaderSection(isLoading, errorMessage, weeklyDefi)

            ChallengeImageSection(
                bitmap = bitmap,
                urlImage = urlImage,
                isParticipating = isParticipating,
                onRemoveImage = {
                    bitmap = null
                    viewModel.deleteParticipation(weeklyDefi!!.id)
                }
            )

            ChallengeActionButtons(
                bitmap = bitmap,
                isParticipating = isParticipating,
                hasCameraPermission = hasCameraPermission,
                onRequestPermission = {
                    scope.launch {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onTakePicture = { takePicture.launch(null) },
                onValidate = {
                    if (weeklyDefi != null && bitmap != null) {
                        viewModel.participatingToChallenge(bitmap!!, weeklyDefi!!, userModel)
                        bitmap = null
                    }
                }
            )
        }
    }
}
