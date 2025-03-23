package ca.uqac.friendschallenge.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.model.UserModel
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import ca.uqac.friendschallenge.ui.theme.primaryContainerLight
import ca.uqac.friendschallenge.ui.theme.secondaryLight
import ca.uqac.friendschallenge.ui.theme.tertiaryLight

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogoutButtonClicked: () -> Unit = {},
    userModel: UserModel,
) {
    Box(
        modifier = modifier
            .background(primaryContainerLight)
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

            Spacer(
                modifier = Modifier
                    .background(secondaryLight)
                    .fillMaxWidth()
                    .height(1.dp)
            )

            Spacer(modifier = Modifier.height(150.dp))

            Column() {
                    Text(
                        text = stringResource(R.string.profile_picture),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )

                    PictureGrid()
                }
            }


        }

    }

@Composable
fun PictureGrid() {
    val images = listOf(
        R.drawable.erable,
        R.drawable.fleurs,
        R.drawable.foret,
        R.drawable.photo,
        R.drawable.ponton,
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(images) { image ->
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(tertiaryLight)
            )
        }
    }
}

@Preview
@Composable
fun PreviewProfileScreen() {
    FriendsChallengeTheme {
        ProfileScreen(
            userModel = UserModel("uid", "username", "email"),
            onLogoutButtonClicked = {}
        )
    }
}