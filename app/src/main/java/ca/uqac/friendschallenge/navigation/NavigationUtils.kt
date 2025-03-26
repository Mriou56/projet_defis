package ca.uqac.friendschallenge.navigation

import androidx.navigation.NavHostController

fun NavHostController.navigateAndClearStack(targetScreen: AppScreen) {
    this.navigate(targetScreen.name) {
        popUpTo(graph.id) { inclusive = true }
    }
}

fun shouldShowNavigationBars(screen: AppScreen): Boolean {
    return screen != AppScreen.Login && screen != AppScreen.Register
}