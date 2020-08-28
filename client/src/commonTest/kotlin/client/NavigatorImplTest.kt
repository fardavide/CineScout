package client

import assert4k.*
import client.ViewState
import kotlin.test.*


class NavigatorImplTest {

    private val navigator = NavigatorImpl()

    @Test
    fun `to emit the right screen`() {
        // initial screen
        assert that navigator.screen.data equals Screen.Home

        navigator.to(Screen.Search)
        assert that navigator.screen.data equals Screen.Search

        navigator.to(Screen.Suggestions)
        assert that navigator.screen.data equals Screen.Suggestions

        navigator.to(Screen.Home)
        assert that navigator.screen.data equals Screen.Home
    }

    @Test
    fun `Can handle app closing`() {
        navigator.quit()
        assert that navigator.screen.value equals ViewState.None
    }

    @Test
    fun `Closes when back, if back-stack is empty`() {
        navigator.back()
        assert that navigator.screen.value equals ViewState.None
    }

    @Test
    fun `Closes when back, if Home`() {
        navigator.toSearch()
        navigator.toHome()

        assert that navigator.screen.data equals Screen.Home

        navigator.back()
        assert that navigator.screen.value equals ViewState.None
    }

    @Test
    fun `Can handle back-stack`() {
        navigator.toSearch()
        navigator.toSuggestions()

        assert that navigator.screen.data equals Screen.Suggestions

        navigator.back()
        assert that navigator.screen.data equals Screen.Search

        navigator.back()
        assert that navigator.screen.data equals Screen.Home

        navigator.back()
        assert that navigator.screen.value equals ViewState.None
    }

    @Test
    fun `Does not add multiple time sequentially the same screen in the back-stack`() {
        navigator.toSearch()
        navigator.toSearch()

        assert that navigator.screen.data equals Screen.Search

        navigator.back()
        assert that navigator.screen.data equals Screen.Home
    }

    @Test
    fun `Does not add multiple time not sequentially the same screen in the back-stack`() {
        navigator.toSearch()
        navigator.toSuggestions()
        navigator.toSearch()

        assert that navigator.screen.data equals Screen.Search

        navigator.back()
        assert that navigator.screen.data equals Screen.Suggestions

        navigator.back()
        assert that navigator.screen.data equals Screen.Home
    }
}
