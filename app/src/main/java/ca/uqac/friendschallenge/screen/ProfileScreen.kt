package ca.uqac.friendschallenge.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.model.ImageModel
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import ca.uqac.friendschallenge.viewmodel.ProfileViewModel
import coil.compose.AsyncImage

/**
 * The ProfileScreen composable function displays the profile screen of current application user.
 *
 * @param modifier The modifier to be applied to the root layout.
 * @param onLogoutButtonClicked Callback function to be invoked when the logout button is clicked.
 * @param userModel The user model representing the current user.
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogoutButtonClicked: () -> Unit = {},
    userModel: UserModel,
) {
    val viewModel : ProfileViewModel = viewModel()
    val images by viewModel.images

    // Load images when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }


    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp, end = 16.dp, start = 16.dp),
        ) {

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                // User information section with logout button
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "profile picture",
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp)
                )
                Text(
                    text = userModel.username,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onLogoutButtonClicked,
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "logout",
                    )
                }
            }

            HorizontalDivider()

            Spacer(modifier = Modifier.height(150.dp))

            // Section title for profile pictures
            Column() {
                    Text(
                        text = stringResource(R.string.profile_picture),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )

                PictureGrid(images = images)
                }
            }


        }

    }

/**
 * The PictureGrid composable function displays a grid of images.
 *
 * @param images The list of image models to be displayed in the grid.
 */
@Composable
fun PictureGrid(images: List<ImageModel>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(images) { imageModel ->
            // Display each image with rounded corners
            AsyncImage(
                model = imageModel.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

/**
 * The PreviewProfileScreen composable function provides a preview of the ProfileScreen.
 *
 * @param showBackground Boolean flag indicating whether to show the background.
 */
@Preview
@Composable
fun PreviewProfileScreen(showBackground: Boolean = true) {
    FriendsChallengeTheme {
        ProfileScreen(
            userModel = UserModel("uid", "username", "email", 0.0, 0.0),
            onLogoutButtonClicked = {}
        )
    }
}