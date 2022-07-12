package cinescout.network

import com.soywiz.klock.Date
import com.soywiz.klock.DateFormat
import com.soywiz.klock.parseDate
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

private class DateSerializer : KSerializer<Date> {

    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = DateFormat.FORMAT_DATE.parseDate(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.format(DateFormat.FORMAT_DATE))
    }
}
