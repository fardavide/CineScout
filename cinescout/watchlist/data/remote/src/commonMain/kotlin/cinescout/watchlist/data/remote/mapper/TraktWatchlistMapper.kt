package cinescout.watchlist.data.remote.mapper

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistExtendedResponse
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistMetadataResponse
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper

@Factory
internal class TraktWatchlistMapper(
//    private val metadataMapper: TraktScreenplayMetadataMapper,
    private val screenplayMapper: TraktScreenplayMapper
) {

//    fun toRequest(screenplayId: TmdbScreenplayId, rating: Rating): TraktMultiRatingMetadataBody =
//        toRequest(listOf(ScreenplayIdWithPersonalRating(screenplayId, rating)))
//
//    private fun toRequest(screenplayIds: List<ScreenplayIdWithPersonalRating>): TraktMultiRatingMetadataBody {
//        val movies = screenplayIds.filterIsInstance<MovieIdWithPersonalRating>().map { idWithPersonalRating ->
//            val body = metadataMapper.toMovieMetadataBody(idWithPersonalRating.screenplayId)
//            TraktMovieRatingMetadataBody(movie = body, rating = idWithPersonalRating.personalRating.intValue)
//        }
//        val tvShows = screenplayIds.filterIsInstance<TvShowIdWithPersonalRating>().map { idWithPersonalRating ->
//            val body = metadataMapper.toTvShowMetadataBody(idWithPersonalRating.screenplayId)
//            TraktTvShowRatingMetadataBody(tvShow = body, rating = idWithPersonalRating.personalRating.intValue)
//        }
//        return TraktMultiRatingMetadataBody(
//            movies = movies,
//            tvShows = tvShows
//        )
//    }

    fun toScreenplays(response: TraktScreenplaysWatchlistExtendedResponse): List<Screenplay> =
        response.map { ratingMetadataBody ->
            screenplayMapper.toScreenplay(ratingMetadataBody.screenplay)
        }

    fun toScreenplayIds(response: TraktScreenplaysWatchlistMetadataResponse): List<TmdbScreenplayId> =
        response.map { ratingMetadataBody ->
            ratingMetadataBody.tmdbId
        }
}
