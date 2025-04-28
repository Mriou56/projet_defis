package ca.uqac.friendschallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ca.uqac.friendschallenge.navigation.AppNavigation
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme

/**
 * Main activity for the Friends Challenge application.
 *
 * This activity serves as the entry point for the application and sets up the main content view.
 * It uses Jetpack Compose for UI rendering and applies the FriendsChallenge theme.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FriendsChallengeTheme {
                AppNavigation()
            }
        }
    }
}