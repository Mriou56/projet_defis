package ca.uqac.friendschallenge.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ca.uqac.friendschallenge.screen.*
import ca.uqac.friendschallenge.viewmodel.AuthViewModel
import ca.uqac.friendschallenge.data.AuthState

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    authState: AuthState,
    modifier: Modifier = Modifier
) {
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
