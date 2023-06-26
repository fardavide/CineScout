package cinescout.screenplay.domain.model.id

import co.touchlab.kermit.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable(with = ScreenplayIds.Serializer::class)
sealed interface ScreenplayIds : ContentIds {

    override val tmdb: TmdbScreenplayId
    override val trakt: TraktScreenplayId

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
                MoviePrefix -> MovieIds(
                    tmdb = TmdbMovieId(tmdbId),
                    trakt = TraktMovieId(traktId)
                )
                TvShowPrefix -> TvShowIds(
                    tmdb = TmdbTvShowId(tmdbId),
                    trakt = TraktTvShowId(traktId)
                )
                else -> error("Unknown prefix: $prefix")
            }
        }

        override fun serialize(encoder: Encoder, value: ScreenplayIds) {
            val prefix = when (value) {
                is MovieIds -> MoviePrefix
                is TvShowIds -> TvShowPrefix
            }
            val string = "$prefix$Separator${value.tmdb.value}$Separator${value.trakt.value}"
            Logger.v("serialize ScreenplayIds $value as: $string")
            encoder.encodeString(string)
        }
    }
}

sealed interface TmdbScreenplayId : TmdbContentId

sealed interface TraktScreenplayId : TraktContentId

fun ScreenplayIds(tmdb: TmdbScreenplayId, trakt: TraktScreenplayId) = when (tmdb) {
    is TmdbMovieId -> MovieIds(tmdb, trakt as TraktMovieId)
    is TmdbTvShowId -> TvShowIds(tmdb, trakt as TraktTvShowId)
}
