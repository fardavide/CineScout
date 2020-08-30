package client

import assert4k.*
import client.ViewState
import domain.Test.Movie.Fury
import domain.Test.Movie.TheBookOfEli
import kotlin.test.*


class NavigatorImplTest {

    private val navigator = NavigatorImpl()

    @Test
    fun `to emits the right screen`() {
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
    fun `to emits the right screen with params`() {
        navigator.to(Screen.MovieDetails(Fury))
        assert that navigator.screen.data equals Screen.MovieDetails(Fury)
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

    @Test
    fun `back-stack works properly with screen with different parameters`() {
        navigator.toSearch()
        navigator.toMovieDetails(Fury)
        navigator.toMovieDetails(TheBookOfEli)
        navigator.toSearch()
        navigator.toMovieDetails(Fury)

        assert that navigator.screen.data equals Screen.MovieDetails(Fury)

        navigator.back()
        assert that navigator.screen.data equals Screen.Search

        navigator.back()
        assert that navigator.screen.data equals Screen.MovieDetails(TheBookOfEli)

        navigator.back()
        assert that navigator.screen.data equals Screen.Home
    }
}
