package cinescout.people.data.local.mapper

import cinescout.database.model.DatabaseTmdbPersonId
import cinescout.people.domain.model.TmdbPersonId

internal fun DatabaseTmdbPersonId.toId() = TmdbPersonId(value)

internal fun TmdbPersonId.toDatabaseId() = DatabaseTmdbPersonId(value)
