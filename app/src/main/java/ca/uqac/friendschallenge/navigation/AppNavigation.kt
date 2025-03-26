package ca.uqac.friendschallenge.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ca.uqac.friendschallenge.components.BottomNavigationBar
import ca.uqac.friendschallenge.components.TopBar
import ca.uqac.friendschallenge.viewmodel.AuthViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    val currentScreen = AppScreen.fromRoute(backStackEntry?.destination?.route)

    HandleAuthNavigation(authState.isAuthenticated, navController)
    HandleAuthErrors(authState.errorMessage, context)

    Scaffold(
        topBar = {
            if (shouldShowNavigationBars(currentScreen)) {
                TopBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        bottomBar = {
            if (shouldShowNavigationBars(currentScreen)) {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    onHomeButtonClicked = { navController.navigateAndClearStack(AppScreen.Home) },
                    onProfileButtonClicked = { navController.navigateAndClearStack(AppScreen.Profile) },
                    onFriendsButtonClicked = { navController.navigateAndClearStack(AppScreen.Friends) }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            authViewModel = authViewModel,
            authState = authState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
