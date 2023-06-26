package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TraktMovieId
import korlibs.time.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

typealias TraktMoviesExtendedResponse = List<TraktMovieExtendedBody>

@Serializable
@SerialName(TraktContentType.Movie)
data class TraktMovieExtendedBody(

    @SerialName(TraktContent.Genres)
    override val genreSlugs: List<GenreSlug>,

    @SerialName(TraktContent.Ids)
    override val ids: TraktMovieIds,

    @SerialName(TraktContent.Overview)
    override val overview: String = "",

    @Contextual
    @SerialName(Released)
    val releaseDate: Date?,

    @Contextual
    @SerialName(TraktContent.Runtime)
    override val runtime: Duration? = null,

    @SerialName(Tagline)
    val tagline: String = "",

    @SerialName(TraktContent.Title)
    val title: String,

    @SerialName(TraktContent.Rating)
    override val voteAverage: Double,

    @SerialName(TraktContent.Votes)
    override val voteCount: Int

) : TraktScreenplayExtendedBody {

    override val tmdbId: TmdbMovieId
        get() = ids.tmdb

    override val traktId: TraktMovieId
        get() = ids.trakt

    companion object {

        const val Released = "released"
        const val Tagline = "tagline"
    }
}
