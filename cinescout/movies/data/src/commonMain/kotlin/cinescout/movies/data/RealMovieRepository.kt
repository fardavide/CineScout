package cinescout.movies.data

import arrow.core.Either
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.PagedStore
import cinescout.store.Paging
import cinescout.store.Store
import cinescout.store.distinctUntilDataChanged
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override suspend fun addToWatchlist(movie: Movie) {
        localMovieDataSource.insertWatchlist(movie)
        remoteMovieDataSource.postWatchlist(movie)
    }

    override fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError.Remote, List<Movie>>> =
        Store(
            fetch = { remoteMovieDataSource.discoverMovies(params) },
            read = { flowOf(DataError.Local.NoCache.left()) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getAllRatedMovies(): PagedStore<MovieWithRating, Paging.Page.DualSources> =
        PagedStore<MovieWithRating, Paging.Page.DualSources, Paging.Page.DualSources, Paging.Page.DualSources>(
            initialBookmark = Paging.Page.DualSources.Initial,
            createNextBookmark = { lastData, _ -> lastData.paging + 1 },
            fetch = { page -> remoteMovieDataSource.getRatedMovies(page) },
            read = { localMovieDataSource.findAllRatedMovies() },
            write = { localMovieDataSource.insertRatings(it) }
        ).distinctUntilDataChanged()

    override fun getMovie(id: TmdbMovieId): Flow<Either<DataError, Movie>> =
        Store(
            fetch = { remoteMovieDataSource.getMovie(id) },
            read = { localMovieDataSource.findMovie(id) },
            write = { localMovieDataSource.insert(it) }
        )

    override suspend fun rate(movie: Movie, rating: Rating): Either<DataError, Unit> {
        localMovieDataSource.insertRating(movie, rating)
        return remoteMovieDataSource.postRating(movie, rating).mapLeft { networkError ->
            DataError.Remote(networkError)
        }
    }
}
