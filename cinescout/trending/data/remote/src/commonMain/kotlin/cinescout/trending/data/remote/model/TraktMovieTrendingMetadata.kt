package cinescout.trending.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesTrendingMetadataResponse = List<TraktMovieTrendingMetadataBody>

@Serializable
data class TraktMovieTrendingMetadataBody(
    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody
) : TraktScreenplayTrendingMetadataBody {

    override val tmdbId: TmdbMovieId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
