package cinescout.network

import korlibs.time.Date
import korlibs.time.DateFormat
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal val CineScoutSerializersModule = SerializersModule {
    contextual(DateSerializer())
    contextual(DateTimeSerializer())
    contextual(DurationSerializer())
}

internal class DateSerializer : KSerializer<Date> {

    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date = tryParse(str = decoder.decodeString())
        ?.local
        ?.date
        ?: DateTime.EPOCH.date

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.format(DateFormat.FORMAT_DATE))
    }

    private fun tryParse(str: String): DateTimeTz? =
        DateFormat.FORMAT_DATE.tryParse(str = str, doThrow = false)
            ?: DateFormat.FORMAT2.tryParse(str = str, doThrow = false)
}

internal class DateTimeSerializer : KSerializer<DateTime> {

    override val descriptor = PrimitiveSerialDescriptor("DateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DateTime = tryParse(str = decoder.decodeString())
        ?.local
        ?: DateTime.EPOCH

    override fun serialize(encoder: Encoder, value: DateTime) {
        encoder.encodeString(value.format(DateFormat.FORMAT1))
    }

    private fun tryParse(str: String): DateTimeTz? = DateFormat.FORMAT2.tryParse(str = str, doThrow = false)
        ?: DateFormat.FORMAT1.tryParse(str = str, doThrow = false)
        ?: DateFormat.FORMAT_DATE.tryParse(str = str, doThrow = false)
}

internal class DurationSerializer : KSerializer<Duration> {

    override val descriptor = PrimitiveSerialDescriptor("IntDuration", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Duration = decoder.decodeInt().minutes

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeInt(value.inWholeMinutes.toInt())
    }
}
