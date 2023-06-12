package cinescout.history.domain.store

import cinescout.history.domain.model.HistoryStoreKey
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.store5.MutableStore5

interface HistoryStore : MutableStore5<HistoryStoreKey, ScreenplayHistory, Unit>
