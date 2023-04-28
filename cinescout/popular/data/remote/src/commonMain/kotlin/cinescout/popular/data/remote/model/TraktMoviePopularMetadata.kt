package cinescout.popular.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesPopularMetadataResponse = List<TraktMoviePopularMetadataBody>

@Serializable
data class TraktMoviePopularMetadataBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody

) : TraktScreenplayPopularMetadataBody {

    override val tmdbId: TmdbScreenplayId.Movie
        get() = movie.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = movie.ids.trakt
}
