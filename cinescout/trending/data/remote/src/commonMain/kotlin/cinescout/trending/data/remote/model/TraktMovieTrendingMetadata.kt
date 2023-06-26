package cinescout.trending.data.remote.model

import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody

typealias TraktMoviesTrendingMetadataResponse = List<TraktMovieTrendingMetadataBody>

@Serializable
data class TraktMovieTrendingMetadataBody(
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieMetadataBody
) : TraktScreenplayTrendingMetadataBody {

    override val tmdbId: TmdbMovieId
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
