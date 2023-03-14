package cinescout.tvshows.data.local

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.continuations.either
import cinescout.database.GenreQueries
import cinescout.database.KeywordQueries
import cinescout.database.LikedTvShowQueries
import cinescout.database.TvShowGenreQueries
import cinescout.database.TvShowKeywordQueries
import cinescout.database.TvShowQueries
import cinescout.database.TvShowRatingQueries
import cinescout.database.TvShowRecommendationQueries
import cinescout.database.WatchlistQueries
import cinescout.database.mapper.groupAsTvShowsWithRating
import cinescout.database.util.mapToListOrError
import cinescout.database.util.mapToOneOrError
import cinescout.database.util.suspendTransaction
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Keyword
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.local.mapper.DatabaseTvShowMapper
import cinescout.tvshows.data.local.mapper.toDatabaseId
import cinescout.tvshows.data.local.mapper.toDatabaseRating
import cinescout.tvshows.data.local.mapper.toId
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowGenres
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory(binds = [LocalTvShowDataSource::class])
internal class RealLocalTvShowDataSource(
    private val databaseTvShowMapper: DatabaseTvShowMapper,
    private val genreQueries: GenreQueries,
    private val keywordQueries: KeywordQueries,
    private val likedTvShowQueries: LikedTvShowQueries,
    @Named(DispatcherQualifier.Io) private val readDispatcher: CoroutineDispatcher,
    transacter: Transacter,
    private val tvShowGenreQueries: TvShowGenreQueries,
    private val tvShowKeywordQueries: TvShowKeywordQueries,
    private val tvShowQueries: TvShowQueries,
    private val tvShowRatingQueries: TvShowRatingQueries,
    private val tvShowRecommendationQueries: TvShowRecommendationQueries,
    private val watchlistQueries: WatchlistQueries,
    @Named(DispatcherQualifier.DatabaseWrite) private val writeDispatcher: CoroutineDispatcher
) : LocalTvShowDataSource, Transacter by transacter {

    override suspend fun deleteWatchlist(tvShowId: TmdbTvShowId) {
        watchlistQueries.deleteById(listOf(tvShowId.toDatabaseId()))
    }

    override suspend fun deleteWatchlistExcept(tvShowIds: Collection<TmdbTvShowId>) {
        val databaseTvShowIds = tvShowIds.map { it.toDatabaseId() }
        withContext(writeDispatcher) {
            val all = watchlistQueries.findAllTvShows().executeAsList()
            val toDelete = all.filterNot { it in databaseTvShowIds }
            watchlistQueries.deleteById(toDelete)
        }
    }

    override fun findAllDislikedTvShows(): Flow<List<TvShow>> = tvShowQueries.findAllDisliked()
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map(databaseTvShowMapper::toTvShow) }
        .distinctUntilChanged()

    override fun findAllLikedTvShows(): Flow<List<TvShow>> = tvShowQueries.findAllLiked()
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map(databaseTvShowMapper::toTvShow) }
        .distinctUntilChanged()

    override fun findAllRatedTvShows(): Flow<List<TvShowWithPersonalRating>> =
        tvShowQueries.findAllWithPersonalRating()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                databaseTvShowMapper.toTvShowsWithRating(list.groupAsTvShowsWithRating())
            }

    override fun findAllWatchlistTvShows(): Flow<List<TvShow>> = tvShowQueries.findAllInWatchlist()
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map(databaseTvShowMapper::toTvShow) }

    override fun findRecommendationsFor(tvShowId: TmdbTvShowId): Flow<List<TvShow>> =
        tvShowQueries.findAllRecomendations(tvShowId.toDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.map(databaseTvShowMapper::toTvShow) }
            .distinctUntilChanged()

    override fun findTvShow(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShow>> =
        tvShowQueries.findById(tvShowId.toDatabaseId())
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

    override fun findTvShowWithDetails(tvShowId: TmdbTvShowId): Flow<TvShowWithDetails?> = combine(
        findTvShow(tvShowId),
        findTvShowGenres(tvShowId)
    ) { tvShowEither, genresEither ->
        either {
            TvShowWithDetails(
                tvShow = tvShowEither.bind(),
                genres = genresEither.bind().genres
            )
        }.getOrNull()
    }

    override fun findTvShowKeywords(tvShowId: TmdbTvShowId): Flow<TvShowKeywords> =
        tvShowQueries.findKeywordsByTvShowId(tvShowId.toDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                TvShowKeywords(
                    keywords = list.map { keyword -> Keyword(id = keyword.genreId.toId(), name = keyword.name) },
                    tvShowId = tvShowId
                )
            }

    override fun findTvShowsByQuery(query: String): Flow<List<TvShow>> = tvShowQueries.findAllByQuery(query)
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map(databaseTvShowMapper::toTvShow) }

    override suspend fun insert(tvShow: TvShowWithDetails) {
        suspendTransaction(writeDispatcher) {
            tvShowQueries.insertTvShow(
                firstAirDate = tvShow.tvShow.firstAirDate,
                overview = tvShow.tvShow.overview,
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

    override suspend fun insert(tvShows: Collection<TvShow>) {
        tvShowQueries.suspendTransaction(writeDispatcher) {
            for (tvShow in tvShows) {
                insertTvShow(
                    firstAirDate = tvShow.firstAirDate,
                    overview = tvShow.overview,
                    ratingAverage = tvShow.rating.average.toDatabaseRating(),
                    ratingCount = tvShow.rating.voteCount.toLong(),
                    title = tvShow.title,
                    tmdbId = tvShow.tmdbId.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertDisliked(tvShowId: TmdbTvShowId) {
        likedTvShowQueries.suspendTransaction(writeDispatcher) {
            insert(tvShowId.toDatabaseId(), isLiked = false)
        }
    }

    override suspend fun insertKeywords(keywords: TvShowKeywords) {
        suspendTransaction(writeDispatcher) {
            for (keyword in keywords.keywords) {
                keywordQueries.insertKeyword(
                    tmdbId = keyword.id.toDatabaseId(),
                    name = keyword.name
                )
                tvShowKeywordQueries.insertKeyword(
                    tvShowId = keywords.tvShowId.toDatabaseId(),
                    keywordId = keyword.id.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertLiked(tvShowId: TmdbTvShowId) {
        likedTvShowQueries.suspendTransaction(writeDispatcher) {
            insert(tvShowId.toDatabaseId(), isLiked = true)
        }
    }

    override suspend fun insertRating(tvShowId: TmdbTvShowId, rating: Rating) {
        tvShowRatingQueries.suspendTransaction(writeDispatcher) {
            insertRating(tmdbId = tvShowId.toDatabaseId(), rating = rating.toDatabaseRating())
        }
    }

    override suspend fun insertRatings(tvShowsWithRating: Collection<TvShowWithPersonalRating>) {
        suspendTransaction(writeDispatcher) {
            for (tvShowWithRating in tvShowsWithRating) {
                val databaseTmdbTvShowId = tvShowWithRating.tvShow.tmdbId.toDatabaseId()
                tvShowQueries.insertTvShow(
                    firstAirDate = tvShowWithRating.tvShow.firstAirDate,
                    overview = tvShowWithRating.tvShow.overview,
                    ratingAverage = tvShowWithRating.tvShow.rating.average.toDatabaseRating(),
                    ratingCount = tvShowWithRating.tvShow.rating.voteCount.toLong(),
                    title = tvShowWithRating.tvShow.title,
                    tmdbId = databaseTmdbTvShowId
                )
                tvShowRatingQueries.insertRating(
                    tmdbId = databaseTmdbTvShowId,
                    rating = tvShowWithRating.personalRating.toDatabaseRating()
                )
            }
        }
    }

    override suspend fun insertRatingIds(ids: Collection<TvShowIdWithPersonalRating>) {
        tvShowRatingQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                insertRating(tmdbId = id.tvShowId.toDatabaseId(), rating = id.personalRating.toDatabaseRating())
            }
        }
    }

    override suspend fun insertRecommendations(tvShowId: TmdbTvShowId, recommendations: List<TvShow>) {
        suspendTransaction(writeDispatcher) {
            for (tvShow in recommendations) {
                val databaseTmdbTvShowId = tvShow.tmdbId.toDatabaseId()
                tvShowQueries.insertTvShow(
                    firstAirDate = tvShow.firstAirDate,
                    overview = tvShow.overview,
                    ratingAverage = tvShow.rating.average.toDatabaseRating(),
                    ratingCount = tvShow.rating.voteCount.toLong(),
                    title = tvShow.title,
                    tmdbId = databaseTmdbTvShowId
                )
                tvShowRecommendationQueries.insertRecommendation(
                    tvShowId = tvShowId.toDatabaseId(),
                    recommendedTvShowId = databaseTmdbTvShowId
                )
            }
        }
    }

    override suspend fun insertWatchlist(tvShowId: TmdbTvShowId) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            insertWatchlist(tvShowId.toDatabaseId())
        }
    }

    override suspend fun insertWatchlist(tvShows: Collection<TvShow>) {
        suspendTransaction(writeDispatcher) {
            for (tvShow in tvShows) {
                tvShowQueries.insertTvShow(
                    firstAirDate = tvShow.firstAirDate,
                    overview = tvShow.overview,
                    ratingAverage = tvShow.rating.average.toDatabaseRating(),
                    ratingCount = tvShow.rating.voteCount.toLong(),
                    title = tvShow.title,
                    tmdbId = tvShow.tmdbId.toDatabaseId()
                )
                watchlistQueries.insertWatchlist(tvShow.tmdbId.toDatabaseId())
            }
        }
    }

    override suspend fun insertWatchlistIds(ids: Collection<TmdbTvShowId>) {
        watchlistQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                insertWatchlist(id.toDatabaseId())
            }
        }
    }
}
