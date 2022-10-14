package cinescout.tvshows.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowGenres
import cinescout.tvshows.domain.model.TvShowWithDetails
import kotlinx.coroutines.flow.Flow

interface LocalTvShowDataSource {

    suspend fun deleteWatchlist(tvShows: Collection<TvShow>)

    fun findAllWatchlistTvShows(): Flow<List<TvShow>>

    fun findTvShow(id: TmdbTvShowId): Flow<Either<DataError.Local, TvShow>>

    fun findTvShowGenres(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShowGenres>>

    fun findTvShowWithDetails(id: TmdbTvShowId): Flow<TvShowWithDetails?>

    suspend fun insert(tvShow: TvShowWithDetails)

    suspend fun insertWatchlist(tvShows: Collection<TvShow>)
}
