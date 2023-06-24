package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseBookmark
import cinescout.database.model.DatabaseDataStatus
import cinescout.database.model.DatabasePage

val BookmarkAdapter = object : ColumnAdapter<DatabaseBookmark, String> {

    val CompleteFetched = "complete_fetched"
    val InitialFetched = "initial_fetched"
    val None = "none"
    val Page = "page"
    val Separator = ":"

    override fun decode(databaseValue: String) = when (databaseValue.substringBefore(":")) {
        CompleteFetched -> DatabaseDataStatus.CompleteFetched
        InitialFetched -> DatabaseDataStatus.InitialFetched
        Page -> DatabasePage(databaseValue.substringAfter(":").toInt())
        None -> DatabaseBookmark.None
        else -> error("Unknown value: $databaseValue")
    }

    override fun encode(value: DatabaseBookmark) = when (value) {
        DatabaseDataStatus.CompleteFetched -> CompleteFetched
        DatabaseDataStatus.InitialFetched -> InitialFetched
        is DatabasePage -> "$Page${Separator}${value.value}"
        DatabaseBookmark.None -> None
    }
}
