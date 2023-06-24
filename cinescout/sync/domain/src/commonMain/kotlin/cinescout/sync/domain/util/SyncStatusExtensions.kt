package cinescout.sync.domain.util

import cinescout.fetchdata.domain.model.Bookmark
import cinescout.fetchdata.domain.model.DataStatus
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.model.Page
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.model.SyncStatus
import korlibs.time.DateTime

fun RequiredSync.toBookmark(): Bookmark = when (this) {
    RequiredSync.Initial -> DataStatus.InitialFetched
    RequiredSync.Complete -> DataStatus.CompleteFetched
}

infix fun FetchData?.with(lastActivity: DateTime): SyncStatus = when (this?.bookmark) {
    null -> RequiredSync.Initial
    DataStatus.CompleteFetched -> if (lastActivity > dateTime) RequiredSync.Initial else SyncNotRequired
    DataStatus.InitialFetched -> if (lastActivity > dateTime) RequiredSync.Initial else RequiredSync.Complete
    // Synced with an outdated bookmark, let's sync again
    Bookmark.None, is Page -> RequiredSync.Initial
}
