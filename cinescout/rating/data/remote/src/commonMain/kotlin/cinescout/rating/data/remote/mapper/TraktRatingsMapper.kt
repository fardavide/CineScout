package cinescout.rating.data.remote.mapper

import cinescout.rating.data.remote.model.OptTraktMovieRatingIdsBody
import cinescout.rating.data.remote.model.OptTraktMultiRatingIdsBody
import cinescout.rating.data.remote.model.OptTraktTvShowRatingIdsBody
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsExtendedResponse
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsMetadataResponse
import cinescout.rating.domain.model.MovieIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.model.TvShowIdWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.getOrThrow
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayIdMapper
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper

@Factory
internal class TraktRatingsMapper(
    private val idMapper: TraktScreenplayIdMapper,
    private val screenplayMapper: TraktScreenplayMapper
) {

    fun toRequest(screenplayId: TmdbScreenplayId, rating: Rating): OptTraktMultiRatingIdsBody =
        toRequest(listOf(ScreenplayIdWithPersonalRating(screenplayId, rating)))

    private fun toRequest(screenplayIds: List<ScreenplayIdWithPersonalRating>): OptTraktMultiRatingIdsBody {
        val movies = screenplayIds.filterIsInstance<MovieIdWithPersonalRating>().map { idWithPersonalRating ->
            val body = idMapper.toMovieIds(idWithPersonalRating.screenplayId)
            OptTraktMovieRatingIdsBody(ids = body, rating = idWithPersonalRating.personalRating.intValue)
        }
        val tvShows = screenplayIds.filterIsInstance<TvShowIdWithPersonalRating>().map { idWithPersonalRating ->
            val body = idMapper.toTvShowIds(idWithPersonalRating.screenplayId)
            OptTraktTvShowRatingIdsBody(ids = body, rating = idWithPersonalRating.personalRating.intValue)
        }
        return OptTraktMultiRatingIdsBody(
            movies = movies,
            tvShows = tvShows
        )
    }

    fun toScreenplays(response: TraktScreenplaysRatingsExtendedResponse): List<ScreenplayWithPersonalRating> =
        response.map { ratingMetadataBody ->
            ScreenplayWithPersonalRating(
                personalRating = Rating.of(ratingMetadataBody.rating).getOrThrow(),
                screenplay = screenplayMapper.toScreenplay(ratingMetadataBody.screenplay)
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
