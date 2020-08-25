package client.android

import client.Navigator
import client.Screen
import client.ViewState
import client.ViewStateFlow
import client.ViewStatePublisher

class AndroidNavigator : Navigator, ViewStatePublisher {

    val screen = ViewStateFlow(Screen.Home)
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
