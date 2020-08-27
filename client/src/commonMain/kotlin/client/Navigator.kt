package client

interface Navigator {

    val screen: ViewStateFlow<Screen>

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

internal class NavigatorImpl : Navigator, ViewStatePublisher {

    override val screen = ViewStateFlow(Screen.Home)
    private val backStack = BackStack(Screen.Home)

    override fun to(screen: Screen) {
        backStack += screen
        this.screen.data = screen
    }

    override fun back() {
        val prev = backStack.prev()

        if (prev != null)
            prev.let { screen.data = it }
        else
            screen.state = ViewState.None
    }

    override fun quit() {
        screen.state = ViewState.None
    }

    private class BackStack(initialValue: Screen) {
        private val underlying = arrayListOf(initialValue)

        operator fun plusAssign(screen: Screen) {
            underlying.apply {
                if (screen == Screen.Home) clear()
                else remove(screen)
                add(screen)
            }
        }

        fun prev(): Screen? {
            underlying.removeLast()
            return underlying.lastOrNull()
        }
    }
}
