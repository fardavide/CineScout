package cinescout.test.mock

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithExtrasTestData
import cinescout.settings.domain.usecase.SetForYouHintShown
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal object CacheManager : KoinComponent {

    fun addSuggestedMovies(movies: List<Movie>) {
        runBlocking {
            with(get<LocalMovieDataSource>()) {
                for (movie in movies) {
                    val movieWithExtras = movie.withExtras()
                    insert(movieWithExtras.movieWithDetails)
                    insertCredits(movieWithExtras.credits)
                    insertKeywords(movieWithExtras.keywords)
                }
            }
            get<MovieRepository>().storeSuggestedMovies(movies)
        }
    }

    fun disableForYouHint() {
        runBlocking {
            get<SetForYouHintShown>().invoke()
        }
    }
}

private fun Movie.withExtras(): MovieWithExtras = when (this) {
    MovieTestData.Inception -> MovieWithExtrasTestData.Inception
    MovieTestData.TheWolfOfWallStreet -> MovieWithExtrasTestData.TheWolfOfWallStreet
    MovieTestData.War -> MovieWithExtrasTestData.War
    else -> throw UnsupportedOperationException("Movie $this is not supported")
}
