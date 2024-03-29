package cinescout.history.data.local.mapper

import cinescout.database.model.id.DatabaseHistoryItemId
import cinescout.history.domain.model.HistoryItemId

fun DatabaseHistoryItemId.toDomainId() = HistoryItemId(value)
fun HistoryItemId.toDatabaseId() = DatabaseHistoryItemId(value)
