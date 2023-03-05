package cinescout.movies.domain.usecase

import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithPersonalRating
import org.koin.core.annotation.Factory
import store.Refresh
import store.Store
import store.builder.storeOf
import kotlin.time.Duration.Companion.minutes

interface GetAllRatedMovies {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(7.minutes)): Store<List<MovieWithPersonalRating>>
}

@Factory
class RealGetAllRatedMovies(
    private val movieRepository: MovieRepository
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Refresh): Store<List<MovieWithPersonalRating>> =
        movieRepository.getAllRatedMovies(refresh)
}

class FakeGetAllRatedMovies(
    private val ratedMovies: List<MovieWithPersonalRating>? = null,
    private val store: Store<List<MovieWithPersonalRating>> =
        ratedMovies?.let { storeOf(it) } ?: storeOf(DataError.Local.NoCache)
) : GetAllRatedMovies {

    override operator fun invoke(refresh: Refresh): Store<List<MovieWithPersonalRating>> = store
}
