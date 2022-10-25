package cinescout.tvshows.data.local

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import cinescout.common.model.Genre
import cinescout.common.model.Keyword
import cinescout.common.model.Rating
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.database.GenreQueries
import cinescout.database.KeywordQueries
import cinescout.database.LikedTvShowQueries
import cinescout.database.PersonQueries
import cinescout.database.SuggestedTvShowQueries
import cinescout.database.TvShowBackdropQueries
import cinescout.database.TvShowCastMemberQueries
import cinescout.database.TvShowCrewMemberQueries
import cinescout.database.TvShowGenreQueries
import cinescout.database.TvShowKeywordQueries
import cinescout.database.TvShowPosterQueries
import cinescout.database.TvShowQueries
import cinescout.database.TvShowRatingQueries
import cinescout.database.TvShowRecommendationQueries
import cinescout.database.TvShowVideoQueries
import cinescout.database.TvShowWatchlistQueries
import cinescout.database.mapper.groupAsTvShowsWithRating
import cinescout.database.util.mapToListOrError
import cinescout.database.util.mapToOneOrError
import cinescout.database.util.suspendTransaction
import cinescout.error.DataError
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.local.mapper.DatabaseTvShowCreditsMapper
import cinescout.tvshows.data.local.mapper.DatabaseTvShowMapper
import cinescout.tvshows.data.local.mapper.DatabaseTvShowVideoMapper
import cinescout.tvshows.data.local.mapper.toDatabaseId
import cinescout.tvshows.data.local.mapper.toDatabaseRating
import cinescout.tvshows.data.local.mapper.toDatabaseVideoResolution
import cinescout.tvshows.data.local.mapper.toDatabaseVideoSite
import cinescout.tvshows.data.local.mapper.toDatabaseVideoType
import cinescout.tvshows.data.local.mapper.toId
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowGenres
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class RealLocalTvShowDataSource(
    private val databaseTvShowMapper: DatabaseTvShowMapper,
    private val databaseTvShowCreditsMapper: DatabaseTvShowCreditsMapper,
    private val databaseTvShowVideoMapper: DatabaseTvShowVideoMapper,
    private val genreQueries: GenreQueries,
    private val keywordQueries: KeywordQueries,
    private val likedTvShowQueries: LikedTvShowQueries,
    private val personQueries: PersonQueries,
    private val readDispatcher: CoroutineDispatcher,
    private val suggestedTvShowQueries: SuggestedTvShowQueries,
    transacter: Transacter,
    private val tvShowBackdropQueries: TvShowBackdropQueries,
    private val tvShowCastMemberQueries: TvShowCastMemberQueries,
    private val tvShowCrewMemberQueries: TvShowCrewMemberQueries,
    private val tvShowGenreQueries: TvShowGenreQueries,
    private val tvShowKeywordQueries: TvShowKeywordQueries,
    private val tvShowQueries: TvShowQueries,
    private val tvShowPosterQueries: TvShowPosterQueries,
    private val tvShowRatingQueries: TvShowRatingQueries,
    private val tvShowRecommendationQueries: TvShowRecommendationQueries,
    private val tvShowVideoQueries: TvShowVideoQueries,
    private val watchlistQueries: TvShowWatchlistQueries,
    private val writeDispatcher: CoroutineDispatcher
) : LocalTvShowDataSource, Transacter by transacter {

    override suspend fun deleteWatchlist(tvShowId: TmdbTvShowId) {
        watchlistQueries.deleteById(listOf(tvShowId.toDatabaseId()))
    }

    override suspend fun deleteWatchlist(tvShows: Collection<TvShow>) {
        watchlistQueries.deleteById(tvShows.map { it.tmdbId.toDatabaseId() })
    }

    override fun findAllDislikedTvShows(): Flow<List<TvShow>> =
        tvShowQueries.findAllDisliked()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.map(databaseTvShowMapper::toTvShow) }
            .distinctUntilChanged()
    
    override fun findAllLikedTvShows(): Flow<List<TvShow>> =
        tvShowQueries.findAllLiked()
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

    override fun findAllSuggestedTvShows(): Flow<Either<DataError.Local, NonEmptyList<TvShow>>> =
        tvShowQueries.findAllSuggested()
            .asFlow()
            .mapToListOrError(readDispatcher)
            .map { either -> either.map { list -> list.map(databaseTvShowMapper::toTvShow) } }

    override fun findAllWatchlistTvShows(): Flow<List<TvShow>> =
        tvShowQueries.findAllInWatchlist()
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

    override fun findTvShowCredits(tvShowId: TmdbTvShowId): Flow<TvShowCredits> =
        combine(
            tvShowQueries.findCastByTvShowId(tvShowId.toDatabaseId()).asFlow().mapToList(readDispatcher),
            tvShowQueries.findCrewByTvShowId(tvShowId.toDatabaseId()).asFlow().mapToList(readDispatcher)
        ) { cast, crew ->
            databaseTvShowCreditsMapper.toCredits(tvShowId, cast, crew)
        }

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

    override fun findTvShowImages(tvShowId: TmdbTvShowId): Flow<TvShowImages> =
        combine(
            tvShowBackdropQueries.findAllByTvShowId(tvShowId.toDatabaseId()).asFlow().mapToList(readDispatcher),
            tvShowPosterQueries.findAllByTvShowId(tvShowId.toDatabaseId()).asFlow().mapToList(readDispatcher)
        ) { backdrops, posters ->
            TvShowImages(
                backdrops = backdrops.map { backdrop -> TmdbBackdropImage(path = backdrop.path) },
                posters = posters.map { poster -> TmdbPosterImage(path = poster.path) },
                tvShowId = tvShowId
            )
        }

    override fun findTvShowWithDetails(tvShowId: TmdbTvShowId): Flow<TvShowWithDetails?> =
        combine(
            findTvShow(tvShowId),
            findTvShowGenres(tvShowId)
        ) { tvShowEither, genresEither ->
            either {
                TvShowWithDetails(
                    tvShow = tvShowEither.bind(),
                    genres = genresEither.bind().genres
                )
            }.orNull()
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

    override fun findTvShowsByQuery(query: String): Flow<List<TvShow>> =
        tvShowQueries.findAllByQuery(query)
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.map(databaseTvShowMapper::toTvShow) }

    override fun findTvShowVideos(tvShowId: TmdbTvShowId): Flow<TvShowVideos> =
        tvShowVideoQueries.findAllByTvShowId(tvShowId.toDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> databaseTvShowVideoMapper.toVideos(tvShowId, list) }


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

    override suspend fun insert(tvShows: Collection<TvShow>) {
        tvShowQueries.suspendTransaction(writeDispatcher) {
            for (tvShow in tvShows) {
                insertTvShow(
                    backdropPath = tvShow.backdropImage.orNull()?.path,
                    firstAirDate = tvShow.firstAirDate,
                    overview = tvShow.overview,
                    posterPath = tvShow.posterImage.orNull()?.path,
                    ratingAverage = tvShow.rating.average.toDatabaseRating(),
                    ratingCount = tvShow.rating.voteCount.toLong(),
                    title = tvShow.title,
                    tmdbId = tvShow.tmdbId.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertCredits(credits: TvShowCredits) {
        suspendTransaction(writeDispatcher) {
            for ((index, member) in credits.cast.withIndex()) {
                personQueries.insertPerson(
                    name = member.person.name,
                    profileImagePath = member.person.profileImage.orNull()?.path,
                    tmdbId = member.person.tmdbId.toDatabaseId()
                )
                tvShowCastMemberQueries.insertCastMember(
                    tvShowId = credits.tvShowId.toDatabaseId(),
                    personId = member.person.tmdbId.toDatabaseId(),
                    character = member.character.orNull(),
                    memberOrder = index.toLong()
                )
            }
            for ((index, member) in credits.crew.withIndex()) {
                personQueries.insertPerson(
                    name = member.person.name,
                    profileImagePath = member.person.profileImage.orNull()?.path,
                    tmdbId = member.person.tmdbId.toDatabaseId()
                )
                tvShowCrewMemberQueries.insertCrewMember(
                    tvShowId = credits.tvShowId.toDatabaseId(),
                    personId = member.person.tmdbId.toDatabaseId(),
                    job = member.job.orNull(),
                    memberOrder = index.toLong()
                )
            }
        }
    }

    override suspend fun insertDisliked(tvShowId: TmdbTvShowId) {
        likedTvShowQueries.suspendTransaction(writeDispatcher) {
            insert(tvShowId.toDatabaseId(), isLiked = false)
        }
    }

    override suspend fun insertImages(images: TvShowImages) {
        suspendTransaction(writeDispatcher) {
            for (image in images.backdrops) {
                tvShowBackdropQueries.insertBackdrop(
                    tvShowId = images.tvShowId.toDatabaseId(),
                    path = image.path
                )
            }
            for (image in images.posters) {
                tvShowPosterQueries.insertPoster(
                    tvShowId = images.tvShowId.toDatabaseId(),
                    path = image.path
                )
            }
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
                    backdropPath = tvShowWithRating.tvShow.backdropImage.orNull()?.path,
                    firstAirDate = tvShowWithRating.tvShow.firstAirDate,
                    overview = tvShowWithRating.tvShow.overview,
                    posterPath = tvShowWithRating.tvShow.posterImage.orNull()?.path,
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

    override suspend fun insertRecommendations(tvShowId: TmdbTvShowId, recommendations: List<TvShow>) {
        suspendTransaction(writeDispatcher) {
            for (tvShow in recommendations) {
                val databaseTmdbTvShowId = tvShow.tmdbId.toDatabaseId()
                tvShowQueries.insertTvShow(
                    backdropPath = tvShow.backdropImage.orNull()?.path,
                    firstAirDate = tvShow.firstAirDate,
                    overview = tvShow.overview,
                    posterPath = tvShow.posterImage.orNull()?.path,
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

    override suspend fun insertSuggestedTvShows(tvShows: Collection<TvShow>) {
        suggestedTvShowQueries.suspendTransaction(writeDispatcher) {
            for (tvShow in tvShows) {
                insertSuggestion(tvShow.tmdbId.toDatabaseId(), affinity = 0.0)
            }
        }
    }

    override suspend fun insertVideos(videos: TvShowVideos) {
        suspendTransaction(writeDispatcher) {
            for (video in videos.videos) {
                tvShowVideoQueries.insertVideo(
                    id = video.id.toDatabaseId(),
                    tvShowId = videos.tvShowId.toDatabaseId(),
                    key = video.key,
                    name = video.title,
                    resolution = video.resolution.toDatabaseVideoResolution(),
                    site = video.site.toDatabaseVideoSite(),
                    type = video.type.toDatabaseVideoType()
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
