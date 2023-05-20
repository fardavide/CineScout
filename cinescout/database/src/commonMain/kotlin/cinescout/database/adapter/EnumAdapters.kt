package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseListFilter
import cinescout.database.model.DatabaseListSorting
import cinescout.database.model.DatabaseListType

val ListFilterAdapter = object : EnumAdapter<DatabaseListFilter>(DatabaseListFilter.values()) {}
val ListSortingAdapter = object : EnumAdapter<DatabaseListSorting>(DatabaseListSorting.values()) {}
val ListTypeAdapter = object : EnumAdapter<DatabaseListType>(DatabaseListType.values()) {}

abstract class EnumAdapter<T : Enum<T>>(private val values: Array<T>) : ColumnAdapter<T, String> {

    override fun decode(databaseValue: String): T = values.first { it.name == databaseValue }
    override fun encode(value: T): String = value.name
}
