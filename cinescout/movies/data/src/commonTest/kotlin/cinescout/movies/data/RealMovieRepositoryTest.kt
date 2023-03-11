package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.test.MockStoreOwner

class RealMovieRepositoryTest : BehaviorSpec({
    coroutineTestScope = true

    Given("no cache") {
        val freshMode = MockStoreOwner.Mode.Fresh

        When("discover movies") {
            val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)
            val discoverMoviesParams = DiscoverMoviesParamsSample.FromInception
            val scenario = TestScenario(
                remoteDiscoverMovies = movies,
                storeOwnerMode = freshMode
            )

            Then("movies are emitted from remote source") {
                scenario.sut.discoverMovies(discoverMoviesParams).test {
                    awaitItem() shouldBe movies.right()
                    awaitComplete()
                }
            }
        }
    }
})

private class RealMovieRepositoryTestScenario(
    val sut: RealMovieRepository
)

private fun TestScenario(
    remoteDiscoverMovies: List<Movie>? = null,
    remoteRatedMovieIds: List<MovieIdWithPersonalRating>? = null,
    remoteWatchlistMovieIds: List<TmdbMovieId>? = null,
    storeOwnerMode: MockStoreOwner.Mode
): RealMovieRepositoryTestScenario {
    val remoteMoviesDetails = listOf(
        MovieWithDetailsSample.Inception,
        MovieWithDetailsSample.TheWolfOfWallStreet,
        MovieWithDetailsSample.War
    )
    return RealMovieRepositoryTestScenario(
        sut = RealMovieRepository(
            localMovieDataSource = FakeLocalMovieDataSource(),
            remoteMovieDataSource = FakeRemoteMovieDataSource(
                discoverMovies = remoteDiscoverMovies,
                moviesDetails = remoteMoviesDetails,
                ratedMovieIds = remoteRatedMovieIds,
                watchlistMovieIds = remoteWatchlistMovieIds
            ),
            storeOwner = MockStoreOwner(mode = storeOwnerMode)
        )
    )
}
