package cinescout.search.data.remote.model

import cinescout.screenplay.domain.model.id.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody

typealias TraktMovieSearchExtendedResponse = List<TraktScreenplaySearchExtendedBody>

@Serializable
data class TraktMovieSearchExtendedBody(
    @SerialName(TraktContentType.Movie)
    val movie: TraktMovieExtendedBody
) : TraktScreenplaySearchExtendedBody {

    override val screenplay: TraktMovieExtendedBody
        get() = movie

    override val tmdbId: TmdbMovieId
        get() = movie.tmdbId
}
