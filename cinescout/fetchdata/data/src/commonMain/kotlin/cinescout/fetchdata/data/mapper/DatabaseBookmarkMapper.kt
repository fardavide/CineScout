package cinescout.fetchdata.data.mapper

import cinescout.database.model.DatabaseBookmark
import cinescout.database.model.DatabaseDataStatus
import cinescout.database.model.DatabasePage
import cinescout.fetchdata.domain.model.Bookmark
import cinescout.fetchdata.domain.model.DataStatus
import cinescout.fetchdata.domain.model.Page
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseBookmarkMapper {

    fun toDatabaseBookmark(bookmark: Bookmark): DatabaseBookmark = when (bookmark) {
        Bookmark.None -> DatabaseBookmark.None
        DataStatus.CompleteFetched -> DatabaseDataStatus.CompleteFetched
        DataStatus.InitialFetched -> DatabaseDataStatus.InitialFetched
        is Page -> DatabasePage(bookmark.value)
    }

    fun toBookmark(databaseBookmark: DatabaseBookmark): Bookmark = when (databaseBookmark) {
        DatabaseBookmark.None -> Bookmark.None
        DatabaseDataStatus.CompleteFetched -> DataStatus.CompleteFetched
        DatabaseDataStatus.InitialFetched -> DataStatus.InitialFetched
        is DatabasePage -> Page(databaseBookmark.value)
    }
}
