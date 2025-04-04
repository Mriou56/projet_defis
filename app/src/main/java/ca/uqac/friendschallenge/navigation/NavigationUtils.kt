package ca.uqac.friendschallenge.navigation

import androidx.navigation.NavHostController

fun NavHostController.navigateAndClearStack(targetScreen: AppScreen) {
    this.navigate(targetScreen.name) {
        popUpTo(graph.id) { inclusive = true }
    }
}

fun shouldShowNavigationBars(screen: AppScreen, isAuthenticated: Boolean): Boolean {
    return when (screen) {
        AppScreen.Login, AppScreen.Register -> false
        AppScreen.Progress -> isAuthenticated
        else -> true
    }
}