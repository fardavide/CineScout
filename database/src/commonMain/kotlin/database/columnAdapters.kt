package database

import com.squareup.sqldelight.ColumnAdapter
import database.stats.StatType
import entities.IntId
import entities.TmdbId
import entities.TmdbStringId
import entities.field.Name
import entities.field.Video
import util.equalsNoCase

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

internal class TmdbStringIdAdapter: ColumnAdapter<TmdbStringId, String> {

    override fun decode(databaseValue: String) = TmdbStringId(databaseValue)
    override fun encode(value: TmdbStringId) = value.s
}

internal class NameAdapter: ColumnAdapter<Name, String> {

    override fun decode(databaseValue: String) = Name(databaseValue)
    override fun encode(value: Name) = value.s
}

internal class SiteAdapter: ColumnAdapter<Video.Site, String> {

    override fun decode(databaseValue: String) =
        Video.Site.values().first { it.name equalsNoCase databaseValue }
    override fun encode(value: Video.Site) = value.name.toLowerCase()
}

internal class StatTypeAdapter: ColumnAdapter<StatType, String> {

    override fun decode(databaseValue: String) =
        StatType.values().first { it.name equalsNoCase databaseValue }
    override fun encode(value: StatType) = value.name.toLowerCase()
}

internal class VideoTypeAdapter: ColumnAdapter<Video.Type, String> {

    override fun decode(databaseValue: String) =
        Video.Type.values().first { it.name equalsNoCase databaseValue }
    override fun encode(value: Video.Type) = value.name.toLowerCase()
}
