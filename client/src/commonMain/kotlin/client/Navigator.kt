package client

interface Navigator {

    /**
     * Navigate the the given [Screen]
     */
    fun to(screen: Screen)

    /**
     * Navigate to the previous [Screen]
     */
    fun back()

    /**
     * Quit the app
     */
    fun quit()

    fun toHome() {
        to(Screen.Home)
    }

    fun toSearch() {
        to(Screen.Search)
    }

    fun toSuggestions() {
        to(Screen.Suggestions)
    }
}

enum class Screen {
    Home,
    Search,
    Suggestions,
}
