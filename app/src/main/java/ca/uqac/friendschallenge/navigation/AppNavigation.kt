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
import ca.uqac.friendschallenge.viewmodel.HomeScreenViewModel

/**
 * AppNavigation is a composable function that sets up the main navigation for the application.
 * It includes a top bar and bottom navigation bar, and handles navigation between different screens.
 *
 * @param authViewModel The ViewModel responsible for authentication logic.
 * @param homeViewModel The ViewModel responsible for home screen logic.
 */
@Composable
fun AppNavigation(authViewModel: AuthViewModel = viewModel(), homeViewModel: HomeScreenViewModel = viewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val authState by authViewModel.authState.collectAsState()
    val authIsLoading by authViewModel.isLoading.collectAsState()

    val isTimeToVote by homeViewModel.isTimeToVote.collectAsState()
    val homeIsLoading by homeViewModel.isLoading.collectAsState()

    val context = LocalContext.current

    val currentScreen = AppScreen.fromRoute(backStackEntry?.destination?.route)

    HandleNavigation(authState.isAuthenticated, authIsLoading, isTimeToVote, homeIsLoading, navController)
    HandleAuthErrors(authState.errorMessage, context)

    Scaffold(
        // Displays the TopBar only if necessary (depending on screen and user connection)
        topBar = {
            if (shouldShowNavigationBars(currentScreen, authState.isAuthenticated)) {
                TopBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        // Displays the BottomNavigationBar only if necessary (depending on screen and user connection)
        bottomBar = {
            if (shouldShowNavigationBars(currentScreen, authState.isAuthenticated)) {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    onHomeButtonClicked = {
                        homeViewModel.checkIfTimeToVote()
                        // Lecture avec .value permet ici de lire immédiatement la valeur et ne pas attendre la propagation
                        if (homeViewModel.isLoading.value) {
                            navController.navigateAndClearStack(AppScreen.Progress)
                        } else if (isTimeToVote) {
                            navController.navigateAndClearStack(AppScreen.Vote)
                        } else {
                            navController.navigateAndClearStack(AppScreen.Home)
                        }
                    },
                    onProfileButtonClicked = { navController.navigateAndClearStack(AppScreen.Profile) },
                    onFriendsButtonClicked = { navController.navigateAndClearStack(AppScreen.Friends) }
                )
            }
        }
    ) { innerPadding ->
        // The content of the screen is displayed here
        AppNavHost(
            navController = navController,
            authViewModel = authViewModel,
            authState = authState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
