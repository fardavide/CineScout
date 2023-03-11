package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.ids
import cinescout.movies.domain.store.RatedMovieIdsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [RatedMovieIdsStore::class])
class RealRatedMovieIdsStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : RatedMovieIdsStore,
    Store5<Unit, List<MovieIdWithPersonalRating>> by Store5Builder
        .from<Unit, List<MovieIdWithPersonalRating>>(
            fetcher = EitherFetcher.ofOperation { remoteMovieDataSource.getRatedMovies() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localMovieDataSource.findAllRatedMovies().ids() },
                writer = { _, value -> localMovieDataSource.insertRatingIds(value) }
            )
        )
        .build()
