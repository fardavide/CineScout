package cinescout.tvshows.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Rating
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface LocalTvShowDataSource {

    suspend fun deleteWatchlist(tvShowId: TmdbTvShowId)

    suspend fun deleteWatchlistExcept(tvShowIds: Collection<TmdbTvShowId>)

    fun findAllDislikedTvShows(): Flow<List<TvShow>>

    fun findAllLikedTvShows(): Flow<List<TvShow>>

    fun findAllRatedTvShows(): Flow<List<TvShowWithPersonalRating>>

    fun findAllWatchlistTvShows(): Flow<List<TvShow>>

    fun findRecommendationsFor(tvShowId: TmdbTvShowId): Flow<List<TvShow>>

    fun findTvShow(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShow>>

    fun findTvShowCredits(tvShowId: TmdbTvShowId): Flow<TvShowCredits>

    fun findTvShowGenres(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShowGenres>>

    fun findTvShowImages(tvShowId: TmdbTvShowId): Flow<TvShowImages>

    fun findTvShowWithDetails(tvShowId: TmdbTvShowId): Flow<TvShowWithDetails?>

    fun findTvShowKeywords(tvShowId: TmdbTvShowId): Flow<TvShowKeywords>

    fun findTvShowsByQuery(query: String): Flow<List<TvShow>>

    fun findTvShowVideos(tvShowId: TmdbTvShowId): Flow<TvShowVideos>

    suspend fun insert(tvShow: TvShowWithDetails)

    suspend fun insert(tvShows: Collection<TvShow>)

    suspend fun insertCredits(credits: TvShowCredits)

    suspend fun insertDisliked(tvShowId: TmdbTvShowId)

    suspend fun insertImages(images: TvShowImages)

    suspend fun insertKeywords(keywords: TvShowKeywords)

    suspend fun insertLiked(tvShowId: TmdbTvShowId)

    suspend fun insertRating(tvShowId: TmdbTvShowId, rating: Rating)

    suspend fun insertRatings(tvShowsWithRating: Collection<TvShowWithPersonalRating>)

    suspend fun insertRecommendations(tvShowId: TmdbTvShowId, recommendations: List<TvShow>)

    suspend fun insertVideos(videos: TvShowVideos)

    suspend fun insertWatchlist(tvShowId: TmdbTvShowId)

    suspend fun insertWatchlist(tvShows: Collection<TvShow>)
}

class FakeLocalTvShowDataSource(
    cachedTvShows: List<TvShow> = emptyList(),
    cachedTvShowWithDetails: List<TvShowWithDetails> = emptyList(),
    cachedRatedTvShows: List<TvShowWithPersonalRating> = emptyList(),
    cachedWatchlistTvShows: List<TvShow> = emptyList()
) : LocalTvShowDataSource {

    private val mutableCachedTvShows = MutableStateFlow(cachedTvShows)
    private val mutableCachedTvShowWithDetails = MutableStateFlow(cachedTvShowWithDetails)
    private val mutableCachedRatedTvShows = MutableStateFlow(cachedRatedTvShows)
    private val mutableCachedWatchlistTvShows = MutableStateFlow(cachedWatchlistTvShows)

    override suspend fun deleteWatchlist(tvShowId: TmdbTvShowId) {
        mutableCachedWatchlistTvShows.emit(mutableCachedWatchlistTvShows.value.filterNot { it.tmdbId == tvShowId })
    }

    override suspend fun deleteWatchlistExcept(tvShowIds: Collection<TmdbTvShowId>) {
        mutableCachedWatchlistTvShows.emit(mutableCachedWatchlistTvShows.value.filter { it.tmdbId in tvShowIds })
    }

    override fun findAllDislikedTvShows(): Flow<List<TvShow>> {
        TODO("Not yet implemented")
    }

    override fun findAllLikedTvShows(): Flow<List<TvShow>> {
        TODO("Not yet implemented")
    }

    override fun findAllRatedTvShows(): Flow<List<TvShowWithPersonalRating>> = mutableCachedRatedTvShows

    override fun findAllWatchlistTvShows(): Flow<List<TvShow>> = mutableCachedWatchlistTvShows

    override fun findRecommendationsFor(tvShowId: TmdbTvShowId): Flow<List<TvShow>> {
        TODO("Not yet implemented")
    }

    override fun findTvShow(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShow>> {
        TODO("Not yet implemented")
    }

    override fun findTvShowCredits(tvShowId: TmdbTvShowId): Flow<TvShowCredits> {
        TODO("Not yet implemented")
    }

    override fun findTvShowGenres(tvShowId: TmdbTvShowId): Flow<Either<DataError.Local, TvShowGenres>> {
        TODO("Not yet implemented")
    }

    override fun findTvShowImages(tvShowId: TmdbTvShowId): Flow<TvShowImages> {
        TODO("Not yet implemented")
    }

    override fun findTvShowWithDetails(tvShowId: TmdbTvShowId): Flow<TvShowWithDetails?> =
        mutableCachedTvShowWithDetails.map { tvShows ->
            tvShows.find { it.tvShow.tmdbId == tvShowId }
        }

    override fun findTvShowKeywords(tvShowId: TmdbTvShowId): Flow<TvShowKeywords> {
        TODO("Not yet implemented")
    }

    override fun findTvShowsByQuery(query: String): Flow<List<TvShow>> {
        TODO("Not yet implemented")
    }

    override fun findTvShowVideos(tvShowId: TmdbTvShowId): Flow<TvShowVideos> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(tvShow: TvShowWithDetails) {
        mutableCachedTvShowWithDetails.emit((mutableCachedTvShowWithDetails.value + tvShow).distinct())
    }

    override suspend fun insert(tvShows: Collection<TvShow>) {
        mutableCachedTvShows.emit((mutableCachedTvShows.value + tvShows).distinct())
    }

    override suspend fun insertCredits(credits: TvShowCredits) {
        TODO("Not yet implemented")
    }

    override suspend fun insertDisliked(tvShowId: TmdbTvShowId) {
        TODO("Not yet implemented")
    }

    override suspend fun insertImages(images: TvShowImages) {
        TODO("Not yet implemented")
    }

    override suspend fun insertKeywords(keywords: TvShowKeywords) {
        TODO("Not yet implemented")
    }

    override suspend fun insertLiked(tvShowId: TmdbTvShowId) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRating(tvShowId: TmdbTvShowId, rating: Rating) {
        val tvShow = requireCachedTvShow(tvShowId)
        val tvShowWithPersonalRating = TvShowWithPersonalRating(tvShow, rating)
        mutableCachedRatedTvShows.emit((mutableCachedRatedTvShows.value + tvShowWithPersonalRating).distinct())
    }

    override suspend fun insertRatings(tvShowsWithRating: Collection<TvShowWithPersonalRating>) {
        mutableCachedRatedTvShows.emit((mutableCachedRatedTvShows.value + tvShowsWithRating).distinct())
    }

    override suspend fun insertRecommendations(tvShowId: TmdbTvShowId, recommendations: List<TvShow>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertVideos(videos: TvShowVideos) {
        TODO("Not yet implemented")
    }

    override suspend fun insertWatchlist(tvShowId: TmdbTvShowId) {
        val tvShow = requireCachedTvShow(tvShowId)
        mutableCachedWatchlistTvShows.emit((mutableCachedWatchlistTvShows.value + tvShow).distinct())
    }

    override suspend fun insertWatchlist(tvShows: Collection<TvShow>) {
        mutableCachedWatchlistTvShows.emit((mutableCachedWatchlistTvShows.value + tvShows).distinct())
    }

    private fun findCachedTvShow(id: TmdbTvShowId): TvShow? =
        mutableCachedTvShows.value.find { it.tmdbId == id }

    private fun requireCachedTvShow(id: TmdbTvShowId): TvShow =
        checkNotNull(findCachedTvShow(id)) { "TvShow with id $id not found" }
}
