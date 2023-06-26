package cinescout.rating.data.remote.mapper

import cinescout.rating.data.remote.model.OptTraktMovieRatingIdsBody
import cinescout.rating.data.remote.model.OptTraktMultiRatingIdsBody
import cinescout.rating.data.remote.model.OptTraktTvShowRatingIdsBody
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsExtendedResponse
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsMetadataResponse
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithGenreSlugsAndPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TmdbTvShowId
import cinescout.utils.kotlin.nonEmptyUnsafe
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayIdMapper
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper

@Factory
internal class TraktRatingsMapper(
    private val idMapper: TraktScreenplayIdMapper,
    private val screenplayMapper: TraktScreenplayMapper
) {

    fun toRequest(screenplayId: TmdbScreenplayId, rating: Rating): OptTraktMultiRatingIdsBody {
        val (movie, tvShow) = when (screenplayId) {
            is TmdbMovieId -> {
                val body = idMapper.toOptTraktMovieIds(screenplayId)
                OptTraktMovieRatingIdsBody(ids = body, rating = rating.intValue) to null
            }
            is TmdbTvShowId -> {
                val body = idMapper.toOptTraktTvShowIds(screenplayId)
                null to OptTraktTvShowRatingIdsBody(ids = body, rating = rating.intValue)
            }
        }
        return OptTraktMultiRatingIdsBody(
            movies = listOfNotNull(movie),
            tvShows = listOfNotNull(tvShow)
        )
    }

    fun toScreenplays(
        response: TraktScreenplaysRatingsExtendedResponse
    ): List<ScreenplayWithGenreSlugsAndPersonalRating> = response.map { ratingMetadataBody ->
        ScreenplayWithGenreSlugsAndPersonalRating(
            genreSlugs = ratingMetadataBody.screenplay.genreSlugs.nonEmptyUnsafe(),
            personalRating = Rating.of(ratingMetadataBody.rating).getOrThrow(),
            screenplay = screenplayMapper.toScreenplayWithGenreSlugs(ratingMetadataBody.screenplay).screenplay
        )
    }

    fun toScreenplayIds(
        response: TraktScreenplaysRatingsMetadataResponse
    ): List<ScreenplayIdWithPersonalRating> = response.map { ratingMetadataBody ->
        ScreenplayIdWithPersonalRating(
            personalRating = Rating.of(ratingMetadataBody.rating).getOrThrow(),
            screenplayIds = ratingMetadataBody.ids
        )
    }
}
