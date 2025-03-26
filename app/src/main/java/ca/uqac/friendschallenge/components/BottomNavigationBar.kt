package ca.uqac.friendschallenge.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.navigation.AppScreen
import ca.uqac.friendschallenge.R

@Composable
fun BottomNavigationBar(
    currentScreen: AppScreen,
    onHomeButtonClicked: () -> Unit,
    onFriendsButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(72.dp)
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(id = R.string.home_button),
                    modifier = Modifier.size(32.dp)
                )
            },
            selected = currentScreen == AppScreen.Home,
            onClick = onHomeButtonClicked
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = stringResource(id = R.string.friends_button),
                    modifier = Modifier.size(32.dp)
                )
            },
            selected = currentScreen == AppScreen.Friends,
            onClick = onFriendsButtonClicked
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.profile_button),
                    modifier = Modifier.size(32.dp)
                )
            },
            selected = currentScreen == AppScreen.Profile,
            onClick = onProfileButtonClicked
        )
    }
}