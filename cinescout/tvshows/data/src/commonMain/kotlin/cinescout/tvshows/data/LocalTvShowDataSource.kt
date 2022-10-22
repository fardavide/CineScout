package cinescout.tvshows.data

import arrow.core.Either
import arrow.core.NonEmptyList
import cinescout.common.model.Rating
import cinescout.error.DataError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowGenres
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import kotlinx.coroutines.flow.Flow

interface LocalTvShowDataSource {

    suspend fun deleteWatchlist(tvShowId: TmdbTvShowId)

    suspend fun deleteWatchlist(tvShows: Collection<TvShow>)

    fun findAllDislikedTvShows(): Flow<List<TvShow>>

    fun findAllLikedTvShows(): Flow<List<TvShow>>

    fun findAllRatedTvShows(): Flow<List<TvShowWithPersonalRating>>

    fun findAllSuggestedTvShows(): Flow<Either<DataError.Local, NonEmptyList<TvShow>>>

    fun findAllWatchlistTvShows(): Flow<List<TvShow>>

    fun findRecommendationsFor(tvShowId: TmdbTvShowId): Flow<List<TvShow>>

    fun findTvShow(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShow>>

    fun findTvShowCredits(tvShowId: TmdbTvShowId): Flow<TvShowCredits>

    fun findTvShowGenres(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShowGenres>>

    fun findTvShowImages(tvShowId: TmdbTvShowId): Flow<TvShowImages>

    fun findTvShowWithDetails(tvShowId: TmdbTvShowId): Flow<TvShowWithDetails?>

    fun findTvShowKeywords(tvShowId: TmdbTvShowId): Flow<TvShowKeywords>

    fun findTvShowVideos(tvShowId: TmdbTvShowId): Flow<TvShowVideos>

    suspend fun insert(tvShow: TvShowWithDetails)

    suspend fun insertCredits(credits: TvShowCredits)

    suspend fun insertDisliked(tvShowId: TmdbTvShowId)

    suspend fun insertImages(images: TvShowImages)

    suspend fun insertKeywords(keywords: TvShowKeywords)

    suspend fun insertLiked(tvShowId: TmdbTvShowId)

    suspend fun insertRating(tvShowId: TmdbTvShowId, rating: Rating)

    suspend fun insertRatings(tvShowsWithRating: Collection<TvShowWithPersonalRating>)

    suspend fun insertRecommendations(tvShowId: TmdbTvShowId, recommendations: List<TvShow>)

    suspend fun insertSuggestedTvShows(tvShows: Collection<TvShow>)

    suspend fun insertVideos(videos: TvShowVideos)

    suspend fun insertWatchlist(tvShowId: TmdbTvShowId)

    suspend fun insertWatchlist(tvShows: Collection<TvShow>)
}
