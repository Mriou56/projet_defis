package ca.uqac.friendschallenge

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ca.uqac.friendschallenge.ui.FriendScreen
import ca.uqac.friendschallenge.ui.HomeScreen
import ca.uqac.friendschallenge.ui.LoginScreen
import ca.uqac.friendschallenge.ui.ProfileScreen
import ca.uqac.friendschallenge.ui.RegisterScreen
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme

enum class MainScreen() {
    Login,
    Register,
    Home,
    Profile,
    Friends
}

@Composable
fun MainAppBar(
    currentScreen: MainScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun MainBottomBar(
    currentScreen: MainScreen,
    onHomeButtonClicked: () -> Unit,
    onFriendsButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit,
) {
    NavigationBar(
        modifier = Modifier.height(72.dp)
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(id = R.string.home_button),
                    modifier = Modifier.size(32.dp)
                )
            },
            selected = currentScreen == MainScreen.Home,
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
            selected = currentScreen == MainScreen.Friends,
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
            selected = currentScreen == MainScreen.Profile,
            onClick = onProfileButtonClicked
        )
    }
}

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = MainScreen.valueOf(
        backStackEntry?.destination?.route ?: MainScreen.Login.name
    )

    Scaffold(
        topBar = {
            // On affiche la barre de navigation seulement si on est pas sur l'écran de login ou de register
            if (currentScreen != MainScreen.Login && currentScreen != MainScreen.Register) {
                MainAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        bottomBar = {
            // On affiche la barre de navigation si on est pas sur l'écran de login ou de register
            if (currentScreen != MainScreen.Login && currentScreen != MainScreen.Register) {
                MainBottomBar(
                    currentScreen = currentScreen,
                    onHomeButtonClicked = {
                        navigateWithClearingStack(navController, MainScreen.Home)
                    },
                    onProfileButtonClicked = {
                        navigateWithClearingStack(navController, MainScreen.Profile)
                    },
                    onFriendsButtonClicked = {
                        navigateWithClearingStack(navController, MainScreen.Friends)
                    },
                )
            }
        }


    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = MainScreen.Login.name){
                LoginScreen(
                    onLoginButtonClicked = {
                        navigateWithClearingStack(navController, MainScreen.Home)
                    },
                    onRegisterButtonClicked = {
                        navController.navigate(MainScreen.Register.name)
                    },
                    modifier = Modifier,
                )
            }

            composable(route = MainScreen.Register.name){
                RegisterScreen(
                    onLoginButtonClicked = {
                        navController.navigate(MainScreen.Login.name)
                    },
                    onRegisterButtonClicked = {
                        navigateWithClearingStack(navController, MainScreen.Home)
                    },
                    modifier = Modifier,
                )
            }

            composable(route = MainScreen.Home.name){
                HomeScreen()
            }

            composable(route = MainScreen.Profile.name){
                ProfileScreen()
            }

            composable(route = MainScreen.Friends.name){
                FriendScreen()
            }
        }
    }
}

private fun navigateWithClearingStack(navController: NavHostController, screenToNavigate: MainScreen) {
    navController.navigate(screenToNavigate.name) {
        // On clear le backstack
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }
}

@Preview
@Composable
fun PreviewNavitgationBar() {
    FriendsChallengeTheme {
        MainBottomBar(
            currentScreen = MainScreen.Home,
            onHomeButtonClicked = {},
            onFriendsButtonClicked = {},
            onProfileButtonClicked = {}
        )
    }
}