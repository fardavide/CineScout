package cinescout.search.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMovieSearchExtendedResponse = List<TraktScreenplaySearchExtendedBody>

@Serializable
data class TraktMovieSearchExtendedBody(
    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieExtendedBody
) : TraktScreenplaySearchExtendedBody {

    override val screenplay: TraktMovieExtendedBody
        get() = movie

    override val tmdbId: TmdbMovieId
        get() = movie.tmdbId
}
