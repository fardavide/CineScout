package cinescout.movies.data.remote.tmdb.service

import arrow.core.right
import cinescout.movies.data.remote.tmdb.testdata.DiscoverMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieCreditsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieDetailsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieKeywordsResponseTestData
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.network.CineScoutClient
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieServiceTest {

    private val engine = MockTmdbMovieEngine()
    private val client = CineScoutClient(engine)
    private val service = TmdbMovieService(client = client)

    @Test
    fun `discover movies returns right movies`() = runTest {
        // given
        val expected = DiscoverMoviesResponseTestData.TwoMovies.right()

        // when
        val result = service.discoverMovies(DiscoverMoviesParamsSample.FromInception)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `discover movies uses cast, crew, genres, keyword and release year`() = runTest {
        // given
        val params = DiscoverMoviesParamsSample.FromInception

        // when
        service.discoverMovies(params)

        // then
        val parameters = engine.requestHistory.last().url.parameters
        assertEquals(params.castMember.orNull()?.person?.tmdbId?.value?.toString(), parameters["with_cast"])
        assertEquals(params.crewMember.orNull()?.person?.tmdbId?.value?.toString(), parameters["with_crew"])
        assertEquals(params.genre.orNull()?.id?.value?.toString(), parameters["with_genres"])
        assertEquals(params.keyword.orNull()?.id?.value?.toString(), parameters["with_keywords"])
        assertEquals(params.releaseYear.orNull()?.value?.toString(), parameters["primary_release_year"])
    }

    @Test
    fun `get movie returns right movie`() = runTest {
        // given
        val movie = GetMovieDetailsResponseTestData.Inception
        val expected = movie.right()

        // when
        val result = service.getMovieDetails(movie.id)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie credits returns right credits`() = runTest {
        // given
        val credits = GetMovieCreditsResponseTestData.Inception
        val expected = credits.right()

        // when
        val result = service.getMovieCredits(credits.movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie keywords returns right keywords`() = runTest {
        // given
        val keywords = GetMovieKeywordsResponseTestData.Inception
        val expected = keywords.right()

        // when
        val result = service.getMovieKeywords(MovieSample.Inception.tmdbId)

        // then
        assertEquals(expected, result)
    }
}
