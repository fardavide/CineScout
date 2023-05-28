package cinescout.history.domain.store

import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.model.ScreenplayHistoryStoreKey
import cinescout.store5.MutableStore5

interface ScreenplayHistoryStore : MutableStore5<ScreenplayHistoryStoreKey, ScreenplayHistory, Unit>
