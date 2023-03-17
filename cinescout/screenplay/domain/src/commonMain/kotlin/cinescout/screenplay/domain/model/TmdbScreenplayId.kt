package cinescout.screenplay.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable(TmdbScreenplayId.Serializer::class)
sealed interface TmdbScreenplayId {

    val value: Int

    @JvmInline
    @Serializable
    value class Movie(override val value: Int) : TmdbScreenplayId

    @JvmInline
    @Serializable
    value class TvShow(override val value: Int) : TmdbScreenplayId

    fun uniqueId(): String = Json.encodeToString(Serializer, this)

    object Serializer : KSerializer<TmdbScreenplayId> {

        private const val Separator = ":"
        private const val MoviePrefix = "movie"
        private const val TvShowPrefix = "tv_show"

        override val descriptor = PrimitiveSerialDescriptor("TmdbScreenplayId", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): TmdbScreenplayId {
            val string = decoder.decodeString()
            val (prefix, value) = string.split(Separator)
            val id = value.toInt()
            return when (prefix) {
                MoviePrefix -> Movie(id)
                TvShowPrefix -> TvShow(id)
                else -> error("Unknown prefix: $prefix")
            }
        }

        override fun serialize(encoder: Encoder, value: TmdbScreenplayId) {
            val prefix = when (value) {
                is Movie -> MoviePrefix
                is TvShow -> TvShowPrefix
            }
            encoder.encodeString("$prefix$Separator${value.value}")
        }
    }
}
