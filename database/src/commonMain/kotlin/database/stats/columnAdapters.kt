package database.stats

import com.squareup.sqldelight.ColumnAdapter
import entities.IntId
import entities.Name
import entities.TmdbId
import entities.util.equalsNoCase

internal class UIntAdapter: ColumnAdapter<UInt, Long> {

    override fun decode(databaseValue: Long) = databaseValue.toUInt()
    override fun encode(value: UInt) = value.toLong()
}

internal class IntIdAdapter: ColumnAdapter<IntId, Long> {

    override fun decode(databaseValue: Long) = IntId(databaseValue.toInt())
    override fun encode(value: IntId) = value.i.toLong()
}

internal class TmdbIdAdapter: ColumnAdapter<TmdbId, Long> {

    override fun decode(databaseValue: Long) = TmdbId(databaseValue.toInt())
    override fun encode(value: TmdbId) = value.i.toLong()
}

internal class NameAdapter: ColumnAdapter<Name, String> {

    override fun decode(databaseValue: String) = Name(databaseValue)
    override fun encode(value: Name) = value.s
}

internal class StatTypeAdapter: ColumnAdapter<StatType, String> {

    override fun decode(databaseValue: String) =
        StatType.values().first { it.name equalsNoCase databaseValue }
    override fun encode(value: StatType) = value.name.toLowerCase()
}
