package cinescout.tvshows.data.local

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.continuations.either
import cinescout.common.model.Genre
import cinescout.database.GenreQueries
import cinescout.database.TvShowGenreQueries
import cinescout.database.TvShowQueries
import cinescout.database.TvShowWatchlistQueries
import cinescout.database.util.mapToListOrError
import cinescout.database.util.mapToOneOrError
import cinescout.database.util.suspendTransaction
import cinescout.error.DataError
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.local.mapper.DatabaseTvShowMapper
import cinescout.tvshows.data.local.mapper.toDatabaseId
import cinescout.tvshows.data.local.mapper.toDatabaseRating
import cinescout.tvshows.data.local.mapper.toId
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowGenres
import cinescout.tvshows.domain.model.TvShowWithDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

internal class RealLocalTvShowDataSource(
    private val databaseTvShowMapper: DatabaseTvShowMapper,
    private val genreQueries: GenreQueries,
    private val readDispatcher: CoroutineDispatcher,
    transacter: Transacter,
    private val tvShowGenreQueries: TvShowGenreQueries,
    private val tvShowQueries: TvShowQueries,
    private val watchlistQueries: TvShowWatchlistQueries,
    private val writeDispatcher: CoroutineDispatcher
) : LocalTvShowDataSource, Transacter by transacter {

    override suspend fun deleteWatchlist(tvShows: Collection<TvShow>) {
        watchlistQueries.deleteById(tvShows.map { it.tmdbId.toDatabaseId() })
    }

    override fun findAllWatchlistTvShows(): Flow<List<TvShow>> =
        tvShowQueries.findAllInWatchlist()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.map(databaseTvShowMapper::toTvShow) }

    override fun findTvShow(id: TmdbTvShowId): Flow<Either<DataError.Local, TvShow>> =
        tvShowQueries.findById(id.toDatabaseId())
            .asFlow()
            .mapToOneOrError(readDispatcher)
            .map { either -> either.map { tvShow -> databaseTvShowMapper.toTvShow(tvShow) } }

    override fun findTvShowGenres(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShowGenres>> =
        tvShowQueries.findGenresByTvShowId(tvShowId.toDatabaseId())
            .asFlow()
            .mapToListOrError(readDispatcher)
            .map { either ->
                either.map { list ->
                    TvShowGenres(
                        tvShowId = tvShowId,
                        genres = list.map { genre -> Genre(id = genre.genreId.toId(), name = genre.name) }
                    )
                }
            }

    override fun findTvShowWithDetails(id: TmdbTvShowId): Flow<TvShowWithDetails?> =
        combine(
            findTvShow(id),
            findTvShowGenres(id)
        ) { tvShowEither, genresEither ->
            either {
                TvShowWithDetails(
                    tvShow = tvShowEither.bind(),
                    genres = genresEither.bind().genres
                )
            }.orNull()
        }

    override suspend fun insert(tvShow: TvShowWithDetails) {
        suspendTransaction(writeDispatcher) {
            tvShowQueries.insertTvShow(
                backdropPath = tvShow.tvShow.backdropImage.orNull()?.path,
                firstAirDate = tvShow.tvShow.firstAirDate,
                overview = tvShow.tvShow.overview,
                posterPath = tvShow.tvShow.posterImage.orNull()?.path,
                ratingAverage = tvShow.tvShow.rating.average.toDatabaseRating(),
                ratingCount = tvShow.tvShow.rating.voteCount.toLong(),
                title = tvShow.tvShow.title,
                tmdbId = tvShow.tvShow.tmdbId.toDatabaseId()
            )
            for (genre in tvShow.genres) {
                genreQueries.insertGenre(
                    tmdbId = genre.id.toDatabaseId(),
                    name = genre.name
                )
                tvShowGenreQueries.insertGenre(
                    tvShowId = tvShow.tvShow.tmdbId.toDatabaseId(),
                    genreId = genre.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertWatchlist(tvShows: Collection<TvShow>) {
        suspendTransaction(writeDispatcher) {
            for (tvShow in tvShows) {
                tvShowQueries.insertTvShow(
                    backdropPath = tvShow.backdropImage.orNull()?.path,
                    firstAirDate = tvShow.firstAirDate,
                    overview = tvShow.overview,
                    posterPath = tvShow.posterImage.orNull()?.path,
                    ratingAverage = tvShow.rating.average.toDatabaseRating(),
                    ratingCount = tvShow.rating.voteCount.toLong(),
                    title = tvShow.title,
                    tmdbId = tvShow.tmdbId.toDatabaseId()
                )
                watchlistQueries.insertWatchlist(tvShow.tmdbId.toDatabaseId())
            }
        }
    }
}
