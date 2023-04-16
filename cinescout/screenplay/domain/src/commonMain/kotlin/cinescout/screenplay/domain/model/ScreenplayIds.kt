package cinescout.screenplay.domain.model

import co.touchlab.kermit.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable(with = ScreenplayIds.Serializer::class)
sealed interface ScreenplayIds {

    val tmdb: TmdbScreenplayId
    val trakt: TraktScreenplayId

    @Serializable
    data class Movie(
        override val tmdb: TmdbScreenplayId.Movie,
        override val trakt: TraktScreenplayId.Movie
    ) : ScreenplayIds

    @Serializable
    data class TvShow(
        override val tmdb: TmdbScreenplayId.TvShow,
        override val trakt: TraktScreenplayId.TvShow
    ) : ScreenplayIds

    fun uniqueId(): String = Json.encodeToString(Serializer, this)

    object Serializer : KSerializer<ScreenplayIds> {

        private const val Separator = ":"
        private const val MoviePrefix = "movie"
        private const val TvShowPrefix = "tv_show"

        override val descriptor = PrimitiveSerialDescriptor("ScreenplayIds", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): ScreenplayIds {
            val string = decoder.decodeString()
            Logger.v("deserialize ScreenplayIds: $string")
            val (prefix, tmdb, trakt) = string.split(Separator)
            val tmdbId = tmdb.toInt()
            val traktId = trakt.toInt()
            return when (prefix) {
                MoviePrefix -> Movie(
                    tmdb = TmdbScreenplayId.Movie(tmdbId),
                    trakt = TraktScreenplayId.Movie(traktId)
                )
                TvShowPrefix -> TvShow(
                    tmdb = TmdbScreenplayId.TvShow(tmdbId),
                    trakt = TraktScreenplayId.TvShow(traktId)
                )
                else -> error("Unknown prefix: $prefix")
            }
        }

        override fun serialize(encoder: Encoder, value: ScreenplayIds) {
            val prefix = when (value) {
                is Movie -> MoviePrefix
                is TvShow -> TvShowPrefix
            }
            val string = "$prefix$Separator${value.tmdb.value}$Separator${value.trakt.value}"
            Logger.v("serialize ScreenplayIds $value as: $string")
            encoder.encodeString(string)
        }
    }
}

fun ScreenplayIds(tmdb: TmdbScreenplayId, trakt: TraktScreenplayId) = when (tmdb) {
    is TmdbScreenplayId.Movie -> ScreenplayIds.Movie(tmdb, trakt as TraktScreenplayId.Movie)
    is TmdbScreenplayId.TvShow -> ScreenplayIds.TvShow(tmdb, trakt as TraktScreenplayId.TvShow)
}
