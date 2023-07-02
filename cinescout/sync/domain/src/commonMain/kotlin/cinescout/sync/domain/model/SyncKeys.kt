package cinescout.sync.domain.model

import cinescout.screenplay.domain.model.ScreenplayTypeFilter

@JvmInline value class SyncHistoryKey(val type: ScreenplayTypeFilter)

@JvmInline value class SyncRatingsKey(val type: ScreenplayTypeFilter)

@JvmInline value class SyncWatchlistKey(val type: ScreenplayTypeFilter)

