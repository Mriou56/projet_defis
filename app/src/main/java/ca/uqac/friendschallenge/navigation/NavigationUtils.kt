package ca.uqac.friendschallenge.navigation

import androidx.navigation.NavHostController

/**
 * This file contains utility functions for navigation in the application.
 * It includes functions to navigate to different screens and manage the back stack.
 *
 * @param targetScreen The target screen to navigate to.
 */
fun NavHostController.navigateAndClearStack(targetScreen: AppScreen) {
    this.navigate(targetScreen.name) {
        popUpTo(graph.id) { inclusive = true }
    }
}

/**
 * This function determines whether to show navigation bars based on the current screen and authentication state.
 *
 * @param screen The current screen being displayed.
 * @param isAuthenticated Boolean indicating if the user is authenticated.
 * @return Boolean indicating whether to show navigation bars or not.
 */
fun shouldShowNavigationBars(screen: AppScreen, isAuthenticated: Boolean): Boolean {
    return when (screen) {
        AppScreen.Login, AppScreen.Register -> false
        AppScreen.Progress -> isAuthenticated
        else -> true
    }
}