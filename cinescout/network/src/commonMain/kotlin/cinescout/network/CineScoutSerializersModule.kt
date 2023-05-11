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

internal val CineScoutSerializersModule = SerializersModule {
    contextual(DateSerializer())
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
