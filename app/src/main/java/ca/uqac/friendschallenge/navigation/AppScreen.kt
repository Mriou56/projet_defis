package ca.uqac.friendschallenge.navigation

// This file defines the different screens in the application.
enum class AppScreen {
    Login,
    Register,
    Home,
    Profile,
    Friends,
    Leaderboard,
    Progress,
    Vote;

    // This function returns the name of the screen as a string.
    companion object {
        fun fromRoute(route: String?): AppScreen {
            return entries.firstOrNull { it.name == route } ?: Login
        }
    }
}