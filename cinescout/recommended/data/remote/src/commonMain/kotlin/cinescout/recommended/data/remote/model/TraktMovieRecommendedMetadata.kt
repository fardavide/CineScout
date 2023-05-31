package cinescout.recommended.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesRecommendedMetadataResponse = List<TraktMovieRecommendedMetadataBody>

@Serializable
data class TraktMovieRecommendedMetadataBody(
    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody
) : TraktScreenplayRecommendedMetadataBody {

    override val tmdbId: TmdbMovieId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
