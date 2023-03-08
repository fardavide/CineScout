package cinescout.screenplay.data.local.mapper

import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.screenplay.domain.model.TmdbScreenplayId

fun TmdbScreenplayId.toDatabaseId(): DatabaseTmdbScreenplayId = when (this) {
    is TmdbScreenplayId.Movie -> DatabaseTmdbScreenplayId.Movie(value)
    is TmdbScreenplayId.TvShow -> DatabaseTmdbScreenplayId.TvShow(value)
}
fun DatabaseTmdbScreenplayId.toDomainId(): TmdbScreenplayId = when (this) {
    is DatabaseTmdbScreenplayId.Movie -> TmdbScreenplayId.Movie(value)
    is DatabaseTmdbScreenplayId.TvShow -> TmdbScreenplayId.TvShow(value)
}
