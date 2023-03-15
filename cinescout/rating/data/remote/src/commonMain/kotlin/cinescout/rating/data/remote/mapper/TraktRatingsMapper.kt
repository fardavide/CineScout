package cinescout.rating.data.remote.mapper

import cinescout.rating.data.remote.model.TraktMovieRatingMetadataBody
import cinescout.rating.data.remote.model.TraktMultiRatingMetadataBody
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsMetadataResponse
import cinescout.rating.data.remote.model.TraktTvShowRatingMetadataBody
import cinescout.rating.domain.model.MovieIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.TvShowIdWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.getOrThrow
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMetadataMapper

@Factory
internal class TraktRatingsMapper(
    private val metadataMapper: TraktScreenplayMetadataMapper
) {

    fun toRequest(screenplayId: TmdbScreenplayId, rating: Rating): TraktMultiRatingMetadataBody =
        toRequest(listOf(ScreenplayIdWithPersonalRating(screenplayId, rating)))

    fun toRequest(screenplayIds: List<ScreenplayIdWithPersonalRating>): TraktMultiRatingMetadataBody {
        val movies = screenplayIds.filterIsInstance<MovieIdWithPersonalRating>().map { idWithPersonalRating ->
            val body = metadataMapper.toMovieMetadataBody(idWithPersonalRating.screenplayId)
            TraktMovieRatingMetadataBody(movie = body, rating = idWithPersonalRating.personalRating.intValue)
        }
        val tvShows = screenplayIds.filterIsInstance<TvShowIdWithPersonalRating>().map { idWithPersonalRating ->
            val body = metadataMapper.toTvShowMetadataBody(idWithPersonalRating.screenplayId)
            TraktTvShowRatingMetadataBody(tvShow = body, rating = idWithPersonalRating.personalRating.intValue)
        }
        return TraktMultiRatingMetadataBody(
            movies = movies,
            tvShows = tvShows
        )
    }

    fun toScreenplayIds(
        response: TraktScreenplaysRatingsMetadataResponse
    ): List<ScreenplayIdWithPersonalRating> = response.map { ratingMetadataBody ->
        ScreenplayIdWithPersonalRating(
            personalRating = Rating.of(ratingMetadataBody.rating).getOrThrow(),
            screenplayId = ratingMetadataBody.tmdbId
        )
    }
}
