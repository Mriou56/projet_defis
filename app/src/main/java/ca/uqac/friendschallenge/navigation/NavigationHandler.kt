package ca.uqac.friendschallenge.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import ca.uqac.friendschallenge.utils.ToastUtils


@Composable
fun HandleNavigation(isAuthenticated: Boolean,
                     authIsLoading: Boolean,
                     isTimeToVote: Boolean,
                     homeIsLoading: Boolean,
                     navController: NavHostController
) {
    LaunchedEffect(isAuthenticated, authIsLoading, homeIsLoading, isTimeToVote) {
        if (authIsLoading || homeIsLoading) {
            navController.navigateAndClearStack(AppScreen.Progress)
        } else if (!isAuthenticated) {
            navController.navigateAndClearStack(AppScreen.Login)
        } else if (isTimeToVote) {
            navController.navigateAndClearStack(AppScreen.Vote)
        } else {
            navController.navigateAndClearStack(AppScreen.Home)
        }
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