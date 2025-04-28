package ca.uqac.friendschallenge.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import ca.uqac.friendschallenge.utils.ToastUtils

/**
 * This file contains the navigation handler for the application.
 * It handles the navigation between different screens based on the authentication state and loading states.
 *
 * @param isAuthenticated Boolean indicating if the user is authenticated.
 * @param authIsLoading Boolean indicating if the authentication process is loading.
 * @param isTimeToVote Boolean indicating if it is time to vote.
 * @param homeIsLoading Boolean indicating if the home screen is loading.
 * @param navController The NavHostController used for navigation.
 */
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

/**
 * This function handles the display of authentication error messages.
 * It shows a toast message with the error message if it is not null or empty.
 *
 * @param errorMessage The error message to be displayed.
 * @param context The context used to show the toast message.
 */
@Composable
fun HandleAuthErrors(errorMessage: String?, context: Context) {
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastUtils.show(context, errorMessage)
        }
    }
}