package cinescout.sync.domain.util

import cinescout.fetchdata.domain.model.Bookmark
import cinescout.fetchdata.domain.model.DataStatus
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.model.Page
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.model.SyncStatus

fun RequiredSync.toBookmark(): Bookmark = when (this) {
    RequiredSync.Initial -> DataStatus.InitialFetched
    RequiredSync.Complete -> DataStatus.CompleteFetched
}

fun FetchData?.toSyncStatus(): SyncStatus = when (this?.bookmark) {
    null -> RequiredSync.Initial
    DataStatus.CompleteFetched -> SyncNotRequired
    DataStatus.InitialFetched -> RequiredSync.Complete
    // Synced with an outdated bookmark, let's sync again
    Bookmark.None, is Page -> RequiredSync.Initial
}
