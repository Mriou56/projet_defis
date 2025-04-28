package ca.uqac.friendschallenge.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ca.uqac.friendschallenge.screen.*
import ca.uqac.friendschallenge.viewmodel.AuthViewModel
import ca.uqac.friendschallenge.data.AuthState

/**
 * AppNavHost is a composable function that sets up the navigation host for the application.
 * It defines the different screens and their corresponding routes.
 *
 * @param navController The NavHostController used for navigation.
 * @param authViewModel The ViewModel responsible for authentication logic.
 * @param authState The current authentication state of the user.
 * @param modifier Optional modifier to customize the appearance of the NavHost.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    authState: AuthState,
    modifier: Modifier = Modifier
) {
    // Declaration of the NavHost with the departure route (ProgressScreen)
    NavHost(
        navController = navController,
        startDestination = AppScreen.Progress.name,
        modifier = modifier
    ) {
        composable(route = AppScreen.Login.name) {
            LoginScreen(
                onLoginButtonClicked = { email, password -> authViewModel.login(email, password) },
                onRegisterButtonClicked = { navController.navigate(AppScreen.Register.name) },
                isLoginLoading = authState.isLoading
            )
        }
        composable(route = AppScreen.Register.name) {
            RegisterScreen(
                onLoginButtonClicked = { navController.navigate(AppScreen.Login.name) },
                onRegisterButtonClicked = { email, password, username ->
                    authViewModel.register(email, password, username)
                },
                isRegisterLoading = authState.isLoading
            )
        }

        // Show these screens only if a user is logged in
        composable(route = AppScreen.Home.name) {
            authState.userModel?.let { user ->
                HomeScreen(userModel = user)
            }
        }
        composable(route = AppScreen.Profile.name) {
            authState.userModel?.let { user ->
                ProfileScreen(
                    onLogoutButtonClicked = { authViewModel.logout() },
                    userModel = user
                )
            }
        }
        composable(route = AppScreen.Friends.name) {
            FriendScreen(
                currentUser = authState.userModel ?: error("User not found"),
                onWeekButtonClicked = { navController.navigate(AppScreen.Leaderboard.name) }
            )
        }
        composable(route = AppScreen.Leaderboard.name) {
            LeaderboardScreen(
                currentUser = authState.userModel ?: error("User not found")
            )
        }
        composable(route = AppScreen.Vote.name) {
            VoteScreen(currentUser = authState.userModel ?: error("User not found"))
        }
        composable(route = AppScreen.Progress.name) {
            ProgressScreen()
        }
    }
}
