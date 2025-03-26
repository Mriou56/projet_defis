package ca.uqac.friendschallenge.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import ca.uqac.friendschallenge.utils.ToastUtils
import ca.uqac.friendschallenge.utils.isWeekend


@Composable
fun HandleAuthNavigation(isAuthenticated: Boolean, navController: NavHostController) {
    LaunchedEffect(isAuthenticated) {
        val targetScreen = if (isAuthenticated) {
            if (isWeekend()) AppScreen.Vote else AppScreen.Home
        } else {
            AppScreen.Login
        }
        navController.navigateAndClearStack(targetScreen)
    }
}

@Composable
fun HandleAuthErrors(errorMessage: String?, context: Context) {
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastUtils.show(context, errorMessage)
        }
    }
}