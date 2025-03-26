package ca.uqac.friendschallenge.navigation

enum class AppScreen {
    Login,
    Register,
    Home,
    Profile,
    Friends,
    Leaderboard,
    Vote;

    companion object {
        fun fromRoute(route: String?): AppScreen {
            return entries.firstOrNull { it.name == route } ?: Login
        }
    }
}