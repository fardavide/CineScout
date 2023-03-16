package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseFetchKey

val FetchDataKeyAdapter = object : ColumnAdapter<DatabaseFetchKey, String> {

    override fun decode(databaseValue: String): DatabaseFetchKey {
        val pageString = databaseValue.substringAfter("(").substringBefore(")")
        return when (val page = pageString.toIntOrNull()) {
            null -> DatabaseFetchKey.WithoutPage(databaseValue)
            else -> DatabaseFetchKey.WithPage(databaseValue, page)
        }
    }

    override fun encode(value: DatabaseFetchKey) = when (value) {
        is DatabaseFetchKey.WithPage -> withPage(value.value, value.page)
        is DatabaseFetchKey.WithoutPage -> withoutPage(value.value)
    }

    private fun withPage(value: String, page: Int) = "$value($page)"
    private fun withoutPage(value: String) = value
}
