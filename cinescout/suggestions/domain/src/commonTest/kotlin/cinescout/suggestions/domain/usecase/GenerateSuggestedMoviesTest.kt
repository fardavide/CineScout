package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.SuggestionError
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import cinescout.movies.domain.usecase.GetAllDislikedMovies
import cinescout.movies.domain.usecase.GetAllLikedMovies
import cinescout.movies.domain.usecase.GetAllRatedMovies
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.suggestions.domain.model.SuggestionsMode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import store.builder.dualSourcesEmptyPagedStore
import store.builder.dualSourcesPagedStoreOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GenerateSuggestedMoviesTest {

    private val buildDiscoverMoviesParams: BuildDiscoverMoviesParams = mockk {
        coEvery { this@mockk(any()) } returns DiscoverMoviesParamsTestData.FromInception
    }
    private val getAllDislikedMovies: GetAllDislikedMovies = mockk {
        every { this@mockk() } returns flowOf(emptyList<Movie>().right())
    }
    private val getAllLikedMovies: GetAllLikedMovies = mockk {
        every { this@mockk() } returns flowOf(emptyList<Movie>().right())
    }
    private val getAllRatedMovies: GetAllRatedMovies = mockk {
        every { this@mockk() } returns dualSourcesEmptyPagedStore()
    }
    private val getAllWatchlistMovies: GetAllWatchlistMovies = mockk {
        every { this@mockk() } returns dualSourcesEmptyPagedStore()
    }
    private val getMovieExtras: GetMovieExtras = mockk {
        every { this@mockk(MovieTestData.Inception, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.Inception.right())
        every { this@mockk(MovieTestData.Inception.tmdbId, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.Inception.right())
        every { this@mockk(MovieWithPersonalRatingTestData.Inception, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.Inception.right())

        every { this@mockk(MovieTestData.TheWolfOfWallStreet, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.TheWolfOfWallStreet.right())
        every { this@mockk(MovieTestData.TheWolfOfWallStreet.tmdbId, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.TheWolfOfWallStreet.right())
        every { this@mockk(MovieWithPersonalRatingTestData.TheWolfOfWallStreet, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.TheWolfOfWallStreet.right())

        every { this@mockk(MovieTestData.War, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.War.right())
        every { this@mockk(MovieTestData.War.tmdbId, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.War.right())
        every { this@mockk(MovieWithPersonalRatingTestData.War, refresh = any()) } returns
            flowOf(MovieWithExtrasTestData.War.right())
    }
    private val movieRepository: MovieRepository = mockk()
    private val generateSuggestedMovies = GenerateSuggestedMovies(
        buildDiscoverMoviesParams = buildDiscoverMoviesParams,
        getAllDislikedMovies = getAllDislikedMovies,
        getAllLikedMovies = getAllLikedMovies,
        getAllRatedMovies = getAllRatedMovies,
        getAllWatchlistMovies = getAllWatchlistMovies,
        getMovieExtras = getMovieExtras,
        movieRepository = movieRepository
    )

    @Test
    fun `quick update fetches only the first page of rated movies`() = runTest {
        // given
        val mode = SuggestionsMode.Quick
        val suggestedMoviesPagedStore = spyk(dualSourcesEmptyPagedStore<MovieWithPersonalRating>())
        every { getAllRatedMovies() } returns suggestedMoviesPagedStore

        // when
        generateSuggestedMovies(mode).first()

        // then
        verify(exactly = 0) { suggestedMoviesPagedStore.loadAll() }
    }

    @Test
    fun `quick update fetches only the first page of watchlist movies`() = runTest {
        // given
        val mode = SuggestionsMode.Quick
        val suggestedMoviesPagedStore = spyk(dualSourcesEmptyPagedStore<Movie>())
        every { getAllWatchlistMovies() } returns suggestedMoviesPagedStore

        // when
        generateSuggestedMovies(mode).first()

        // then
        verify(exactly = 0) { suggestedMoviesPagedStore.loadAll() }
    }

    @Test
    fun `deep update fetches all the pages of rated movies`() = runTest {
        // given
        val mode = SuggestionsMode.Deep
        val suggestedMoviesPagedStore = spyk(dualSourcesEmptyPagedStore<MovieWithPersonalRating>())
        every { getAllRatedMovies() } returns suggestedMoviesPagedStore

        // when
        generateSuggestedMovies(mode).first()

        // then
        verify { suggestedMoviesPagedStore.loadAll() }
    }

    @Test
    fun `deep update fetches all the pages of watchlist movies`() = runTest {
        // given
        val mode = SuggestionsMode.Deep
        val suggestedMoviesPagedStore = spyk(dualSourcesEmptyPagedStore<Movie>())
        every { getAllWatchlistMovies() } returns suggestedMoviesPagedStore

        // when
        generateSuggestedMovies(mode).first()

        // then
        verify { suggestedMoviesPagedStore.loadAll() }
    }

    @Test
    fun `when no positive movies`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        every { getAllRatedMovies() } returns dualSourcesEmptyPagedStore()

        // when
        generateSuggestedMovies(SuggestionsMode.Deep).test {

            // the
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `excludes all the known movies`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        val discoveredMovies = listOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        every { movieRepository.discoverMovies(any()) } returns flowOf(discoveredMovies)
        every { getAllDislikedMovies() } returns flowOf(listOf(MovieTestData.War).right())
        every { getAllLikedMovies() } returns flowOf(listOf(MovieTestData.TheWolfOfWallStreet).right())
        every { getAllRatedMovies() } returns dualSourcesPagedStoreOf(listOf(MovieWithPersonalRatingTestData.Inception))

        // when
        generateSuggestedMovies(SuggestionsMode.Deep).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when all suggestions are known movies`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        val discoveredMovies = listOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        every { movieRepository.discoverMovies(any()) } returns flowOf(discoveredMovies)
        every { getAllDislikedMovies() } returns flowOf(listOf(MovieTestData.War).right())
        every { getAllLikedMovies() } returns flowOf(listOf(MovieTestData.TheWolfOfWallStreet).right())
        every { getAllRatedMovies() } returns dualSourcesPagedStoreOf(listOf(MovieWithPersonalRatingTestData.Inception))

        // when
        generateSuggestedMovies(SuggestionsMode.Deep).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when discover returns empty`() = runTest {
        // given
        val expected = SuggestionError.NoSuggestions.left()
        every { movieRepository.discoverMovies(any()) } returns flowOf(emptyList<Movie>().right())

        // when
        generateSuggestedMovies(SuggestionsMode.Deep).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when success`() = runTest {
        // given
        val expected = nonEmptyListOf(
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        val discoveredMovies = listOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet,
            MovieTestData.War
        ).right()
        every { movieRepository.discoverMovies(any()) } returns flowOf(discoveredMovies)
        every { getAllDislikedMovies() } returns flowOf(emptyList<Movie>().right())
        every { getAllLikedMovies() } returns flowOf(emptyList<Movie>().right())
        every { getAllRatedMovies() } returns dualSourcesPagedStoreOf(listOf(MovieWithPersonalRatingTestData.Inception))

        // when
        generateSuggestedMovies(SuggestionsMode.Deep).test {

            // then
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
