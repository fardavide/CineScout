// package cinescout.suggestions.domain.usecase
//
// import app.cash.turbine.test
// import arrow.core.left
// import arrow.core.nonEmptyListOf
// import arrow.core.right
// import cinescout.suggestions.domain.model.SuggestionError
// import cinescout.movies.domain.MovieRepository
// import cinescout.movies.domain.model.Movie
// import cinescout.movies.domain.model.MovieWithPersonalRating
// import cinescout.movies.domain.sample.MovieSample
// import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
// import cinescout.movies.domain.usecase.GetAllDislikedMovies
// import cinescout.movies.domain.usecase.GetAllLikedMovies
// import cinescout.movies.domain.usecase.GetAllRatedMovies
// import cinescout.movies.domain.usecase.GetAllWatchlistMovies
// import cinescout.suggestions.domain.model.SuggestionsMode
// import io.mockk.every
// import io.mockk.mockk
// import io.mockk.spyk
// import io.mockk.verify
// import kotlinx.coroutines.flow.first
// import kotlinx.coroutines.flow.flowOf
// import kotlinx.coroutines.test.runTest
// import store.builder.emptyPagedStore
// import store.builder.pagedStoreOf
// import kotlin.test.Test
// import kotlin.test.assertEquals
//
// internal class GenerateSuggestedMoviesTest {
//
//    private val getAllDislikedMovies: GetAllDislikedMovies = mockk {
//        every { this@mockk() } returns flowOf(emptyList<Movie>())
//    }
//    private val getAllLikedMovies: GetAllLikedMovies = mockk {
//        every { this@mockk() } returns flowOf(emptyList<Movie>())
//    }
//    private val getAllRatedMovies: GetAllRatedMovies = mockk {
//        every { this@mockk(refresh = any()) } returns emptyPagedStore()
//    }
//    private val getAllWatchlistMovies: GetAllWatchlistMovies = mockk {
//        every { this@mockk(refresh = any()) } returns emptyPagedStore()
//    }
//    private val movieRepository: MovieRepository = mockk {
//        every { getRecommendationsFor(movieId = any(), refresh = any()) } returns emptyPagedStore()
//    }
//    private val generateSuggestedMovies = GenerateSuggestedMovies(
//        getAllDislikedMovies = getAllDislikedMovies,
//        getAllLikedMovies = getAllLikedMovies,
//        getAllRatedMovies = getAllRatedMovies,
//        getAllWatchlistMovies = getAllWatchlistMovies,
//        movieRepository = movieRepository
//    )
//
//    @Test
//    fun `quick update fetches only the first page of rated movies`() = runTest {
//        // given
//        val mode = SuggestionsMode.Quick
//        val suggestedMoviesPagedStore = spyk(emptyPagedStore<MovieWithPersonalRating>())
//        every { getAllRatedMovies() } returns suggestedMoviesPagedStore
//
//        // when
//        generateSuggestedMovies(mode).first()
//
//        // then
//        verify(exactly = 0) { suggestedMoviesPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `quick update fetches only the first page of watchlist movies`() = runTest {
//        // given
//        val mode = SuggestionsMode.Quick
//        val suggestedMoviesPagedStore = spyk(emptyPagedStore<Movie>())
//        every { getAllWatchlistMovies() } returns suggestedMoviesPagedStore
//
//        // when
//        generateSuggestedMovies(mode).first()
//
//        // then
//        verify(exactly = 0) { suggestedMoviesPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `deep update fetches all the pages of rated movies`() = runTest {
//        // given
//        val mode = SuggestionsMode.Deep
//        val suggestedMoviesPagedStore = spyk(emptyPagedStore<MovieWithPersonalRating>())
//        every { getAllRatedMovies(refresh = any()) } returns suggestedMoviesPagedStore
//
//        // when
//        generateSuggestedMovies(mode).first()
//
//        // then
//        verify { suggestedMoviesPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `deep update fetches all the pages of watchlist movies`() = runTest {
//        // given
//        val mode = SuggestionsMode.Deep
//        val suggestedMoviesPagedStore = spyk(emptyPagedStore<Movie>())
//        every { getAllWatchlistMovies(refresh = any()) } returns suggestedMoviesPagedStore
//
//        // when
//        generateSuggestedMovies(mode).first()
//
//        // then
//        verify { suggestedMoviesPagedStore.loadAll() }
//    }
//
//    @Test
//    fun `when no positive movies`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        every { getAllRatedMovies() } returns emptyPagedStore()
//
//        // when
//        generateSuggestedMovies(SuggestionsMode.Deep).test {
//
//            // the
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `excludes all the known movies`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        val recommendedMovies = listOf(
//            MovieSample.Inception,
//            MovieSample.TheWolfOfWallStreet,
//            MovieSample.War
//        )
//        every { movieRepository.getRecommendationsFor(movieId = any(), refresh = any()) } returns
//            pagedStoreOf(recommendedMovies)
//        every { getAllDislikedMovies() } returns flowOf(listOf(MovieSample.War))
//        every { getAllLikedMovies() } returns flowOf(listOf(MovieSample.TheWolfOfWallStreet))
//        every { getAllRatedMovies(refresh = any()) } returns
//            pagedStoreOf(listOf(MovieWithPersonalRatingSample.Inception))
//
//        // when
//        generateSuggestedMovies(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `when all suggestions are known movies`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        val recommendedMovies = listOf(
//            MovieSample.Inception,
//            MovieSample.TheWolfOfWallStreet,
//            MovieSample.War
//        )
//        every { movieRepository.getRecommendationsFor(movieId = any(), refresh = any()) } returns
//            pagedStoreOf(recommendedMovies)
//        every { getAllDislikedMovies() } returns flowOf(listOf(MovieSample.War))
//        every { getAllLikedMovies() } returns flowOf(listOf(MovieSample.TheWolfOfWallStreet))
//        every { getAllRatedMovies(refresh = any()) } returns
//            pagedStoreOf(listOf(MovieWithPersonalRatingSample.Inception))
//
//        // when
//        generateSuggestedMovies(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `when recommended returns empty`() = runTest {
//        // given
//        val expected = SuggestionError.NoSuggestions.left()
//        every { movieRepository.getRecommendationsFor(movieId = any(), refresh = any()) } returns emptyPagedStore()
//
//        // when
//        generateSuggestedMovies(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
//
//    @Test
//    fun `when success`() = runTest {
//        // given
//        val expected = nonEmptyListOf(
//            MovieSample.TheWolfOfWallStreet,
//            MovieSample.War
//        ).right()
//        val recommendedMovies = pagedStoreOf(
//            MovieSample.Inception,
//            MovieSample.TheWolfOfWallStreet,
//            MovieSample.War
//        )
//        every { movieRepository.getRecommendationsFor(movieId = any(), refresh = any()) } returns recommendedMovies
//        every { getAllDislikedMovies() } returns flowOf(emptyList())
//        every { getAllLikedMovies() } returns flowOf(emptyList())
//        every { getAllRatedMovies(refresh = any()) } returns
//            pagedStoreOf(listOf(MovieWithPersonalRatingSample.Inception))
//
//        // when
//        generateSuggestedMovies(SuggestionsMode.Deep).test {
//
//            // then
//            assertEquals(expected, awaitItem())
//            awaitComplete()
//        }
//    }
// }
