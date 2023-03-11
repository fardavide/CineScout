package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.store.MovieKeywordsKey
import cinescout.movies.domain.store.MovieKeywordsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [MovieKeywordsStore::class])
class RealMovieKeywordsStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieKeywordsStore,
    Store5<MovieKeywordsKey, MovieKeywords> by Store5Builder
        .from<MovieKeywordsKey, MovieKeywords>(
            fetcher = EitherFetcher.of { key -> remoteMovieDataSource.getMovieKeywords(key.movieId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localMovieDataSource.findMovieKeywords(key.movieId) },
                writer = { _, value -> localMovieDataSource.insertKeywords(value) }
            )
        )
        .build()
