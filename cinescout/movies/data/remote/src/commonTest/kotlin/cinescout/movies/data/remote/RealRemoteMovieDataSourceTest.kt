package cinescout.movies.data.remote

import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import cinescout.auth.domain.usecase.FakeCallWithTraktAccount
import cinescout.model.NetworkOperation
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.movies.domain.sample.MovieIdWithPersonalRatingSample
import cinescout.movies.domain.sample.MovieKeywordsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.screenplay.domain.model.Rating
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.Paging
import store.builder.remotePagedDataOf

class RealRemoteMovieDataSourceTest : BehaviorSpec({

    val page = Paging.Page.Initial

    Given("no account linked") {

        When("discover movies") {
            val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)
            val scenario = TestScenario(discoverMovies = movies)
            val result = scenario.sut.discoverMovies(DiscoverMoviesParamsSample.FromInception)

            Then("movies are returned") {
                result shouldBe movies.right()
            }
        }

        When("get movie credits") {
            val credits = MovieCreditsSample.Inception
            val scenario = TestScenario(movieCredits = credits)
            val result = scenario.sut.getMovieCredits(credits.movieId)

            Then("credits are returned") {
                result shouldBe credits.right()
            }
        }

        When("get movie details") {
            val movie = MovieWithDetailsSample.Inception
            val scenario = TestScenario(movieDetails = movie)
            val result = scenario.sut.getMovieDetails(movie.movie.tmdbId)

            Then("movie is returned") {
                result shouldBe movie.right()
            }
        }

        When("get movie keywords") {
            val keywords = MovieKeywordsSample.Inception
            val scenario = TestScenario(movieKeywords = keywords)
            val result = scenario.sut.getMovieKeywords(keywords.movieId)

            Then("keywords are returned") {
                result shouldBe keywords.right()
            }
        }

        When("get personal recommendations") {
            val scenario = TestScenario()
            val result = scenario.sut.getPersonalRecommendations()

            Then("skipped is returned") {
                result shouldBe NetworkOperation.Skipped.left()
            }
        }

        When("get rated movies") {
            val scenario = TestScenario()
            val result = scenario.sut.getRatedMovies()

            Then("skipped is returned") {
                result shouldBe NetworkOperation.Skipped.left()
            }
        }

        When("get watchlist movies") {
            val scenario = TestScenario()
            val result = scenario.sut.getWatchlistMovies()

            Then("skipped is returned") {
                result shouldBe NetworkOperation.Skipped.left()
            }
        }

        When("post add to watchlist") {
            val scenario = TestScenario()
            val result = scenario.sut.postAddToWatchlist(TmdbMovieIdSample.Inception)

            Then("success is returned") {
                result shouldBe Unit.right()
            }
        }

        When("post rating") {
            val scenario = TestScenario()
            val result = Rating.of(8).toEither().flatMap { rating ->
                scenario.sut.postRating(TmdbMovieIdSample.Inception, rating)
            }

            Then("success is returned") {
                result shouldBe Unit.right()
            }
        }

        When("post remove from watchlist") {
            val scenario = TestScenario()
            val result = scenario.sut.postRemoveFromWatchlist(TmdbMovieIdSample.Inception)

            Then("success is returned") {
                result shouldBe Unit.right()
            }
        }

        When("search movie") {
            val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)
            val scenario = TestScenario(searchMovies = movies)
            val result = scenario.sut.searchMovie("Inception", page = page)

            Then("movies are returned") {
                result shouldBe remotePagedDataOf(*movies.toTypedArray(), paging = page).right()
            }
        }
    }

    Given("Trakt is linked") {

        When("get personal recommendations") {
            val movies = listOf(
                TmdbMovieIdSample.Inception,
                TmdbMovieIdSample.TheWolfOfWallStreet
            )
            val scenario = TestScenario(isTraktLinked = true, personalRecommendations = movies)
            val result = scenario.sut.getPersonalRecommendations()

            Then("movies are returned") {
                result shouldBe listOf(
                    TmdbMovieIdSample.Inception,
                    TmdbMovieIdSample.TheWolfOfWallStreet
                ).right()
            }
        }

        When("get rated movies") {
            val movies = listOf(
                MovieWithPersonalRatingSample.Inception,
                MovieWithPersonalRatingSample.TheWolfOfWallStreet
            )
            val scenario = TestScenario(isTraktLinked = true, ratedMovies = movies)
            val result = scenario.sut.getRatedMovies()

            Then("movies are returned") {
                result shouldBe listOf(
                    MovieIdWithPersonalRatingSample.Inception,
                    MovieIdWithPersonalRatingSample.TheWolfOfWallStreet
                ).right()
            }
        }

        When("get watchlist movies") {
            val movies = listOf(
                MovieSample.Inception,
                MovieSample.TheWolfOfWallStreet
            )
            val scenario = TestScenario(isTraktLinked = true, watchlistMovies = movies)
            val result = scenario.sut.getWatchlistMovies()

            Then("movies are returned") {
                result shouldBe listOf(
                    TmdbMovieIdSample.Inception,
                    TmdbMovieIdSample.TheWolfOfWallStreet
                ).right()
            }
        }

        When("post add to watchlist") {
            val scenario = TestScenario(isTraktLinked = true)
            val result = scenario.sut.postAddToWatchlist(TmdbMovieIdSample.Inception)

            Then("success is returned") {
                result shouldBe Unit.right()
            }

            Then("Trakt source is called") {
                scenario.traktSource.postAddToWatchlistInvoked shouldBe true
            }
        }

        When("post rating") {
            val scenario = TestScenario(isTraktLinked = true)
            val result = Rating.of(8).toEither().flatMap { rating ->
                scenario.sut.postRating(TmdbMovieIdSample.Inception, rating)
            }

            Then("success is returned") {
                result shouldBe Unit.right()
            }

            Then("Trakt source is called") {
                scenario.traktSource.postRatingInvoked shouldBe true
            }
        }

        When("post remove from watchlist") {
            val scenario = TestScenario(isTraktLinked = true)
            val result = scenario.sut.postRemoveFromWatchlist(TmdbMovieIdSample.Inception)

            Then("success is returned") {
                result shouldBe Unit.right()
            }

            Then("Trakt source is called") {
                scenario.traktSource.postRemoveFromWatchlistInvoked shouldBe true
            }
        }
    }
})

private class RealRemoteMovieDataSourceTestScenario(
    val sut: RealRemoteMovieDataSource,
    val traktSource: FakeTraktRemoteMovieDataSource
)

private fun TestScenario(
    discoverMovies: List<Movie>? = null,
    isTraktLinked: Boolean = false,
    movieCredits: MovieCredits? = null,
    movieDetails: MovieWithDetails? = null,
    movieKeywords: MovieKeywords? = null,
    personalRecommendations: List<TmdbMovieId>? = null,
    ratedMovies: List<MovieWithPersonalRating>? = null,
    searchMovies: List<Movie>? = null,
    watchlistMovies: List<Movie>? = null
): RealRemoteMovieDataSourceTestScenario {
    val fakeTmdbSource = FakeTmdbRemoteMovieDataSource(
        discoverMovies = discoverMovies,
        movieCredits = movieCredits,
        movieDetails = movieDetails,
        movieKeywords = movieKeywords,
        searchMovies = searchMovies
    )
    val fakeTraktSource = FakeTraktRemoteMovieDataSource(
        personalRecommendations = personalRecommendations,
        ratedMovies = ratedMovies,
        watchlistMovies = watchlistMovies
    )
    return RealRemoteMovieDataSourceTestScenario(
        sut = RealRemoteMovieDataSource(
            callWithTraktAccount = FakeCallWithTraktAccount(isLinked = isTraktLinked),
            tmdbSource = fakeTmdbSource,
            traktSource = fakeTraktSource
        ),
        traktSource = fakeTraktSource
    )
}
