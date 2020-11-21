package stats.remote

import assert4k.*
import entities.right
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.fullPath
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.Json
import movies.remote.tmdb.model.MoviePageResult
import movies.remote.tmdb.model.MovieResult
import network.test.mockHttpClient
import util.test.CoroutinesTest
import kotlin.test.*

internal class AccountServiceTest : CoroutinesTest {

    private val client = mockHttpClient {
        addHandler { request ->
            val jsonHeader = Headers.build {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            when (val path = request.url.fullPath.substringAfter('/')) {
                "4/account/v4accountId/movie/watchlist?append_to_response=credits&page=1" ->
                    respond(Json.encodeToString(MoviePageResult.serializer(), Page1), headers = jsonHeader)
                "4/account/v4accountId/movie/watchlist?append_to_response=credits&page=2" ->
                    respond(Json.encodeToString(MoviePageResult.serializer(), Page2), headers = jsonHeader)
                "4/account/v4accountId/movie/watchlist?append_to_response=credits&page=3" ->
                    respond(Json.encodeToString(MoviePageResult.serializer(), Page3), headers = jsonHeader)
                else -> error("Unhandled $path")
            }
        }
    }
    private val accountService = AccountService(client, "v3accountId", "v4accountId")

    @Test
    fun `getPagedMoviesWatchlist can emit all the pages correctly`() = coroutinesTest {
        val result = accountService.getPagedMoviesWatchlist().toList()
        assert that result equals listOf(
            listOf(Page1).right(),
            listOf(Page1, Page2).right(),
            listOf(Page1, Page2, Page3).right(),
        )
    }

    private companion object {

        private val FirstMovieResult = MovieResult(
            id = 0,
            title = "first",
            originalTitle = "",
            genreIds = emptyList(),
            originalLanguage = "en",
            posterPath = null,
            backdropPath = null,
            video = false,
            releaseDate = null,
            overview = "",
            adult = false,
            popularity = 0.0,
            voteAverage = 0.0,
            voteCount = 0
        )

        private val SecondMovieResult = FirstMovieResult.copy(title = "second")

        private val ThirdMovieResult = FirstMovieResult.copy(title = "third")

        private val ForthMovieResult = FirstMovieResult.copy(title = "forth")

        private val FifthMovieResult = FirstMovieResult.copy(title = "fifth")

        val Page1 = MoviePageResult(
            page = 1,
            totalPages = 3,
            totalResults = 5,
            results = listOf(
                FirstMovieResult,
                SecondMovieResult
            )
        )

        val Page2 = MoviePageResult(
            page = 2,
            totalPages = 3,
            totalResults = 5,
            results = listOf(
                ThirdMovieResult,
                ForthMovieResult
            )
        )

        val Page3 = MoviePageResult(
            page = 3,
            totalPages = 3,
            totalResults = 5,
            results = listOf(
                FifthMovieResult
            )
        )
    }
}
