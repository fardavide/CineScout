package cinescout.history.data.local.mapper

import cinescout.database.model.DatabaseHistoryItemId
import cinescout.history.domain.model.ScreenplayHistoryItemId

fun DatabaseHistoryItemId.toDomainId() = ScreenplayHistoryItemId(value)
fun ScreenplayHistoryItemId.toDatabaseId() = DatabaseHistoryItemId(value)
