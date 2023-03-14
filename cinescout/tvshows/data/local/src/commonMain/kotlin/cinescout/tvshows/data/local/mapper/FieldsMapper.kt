package cinescout.tvshows.data.local.mapper

import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbKeywordId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.TmdbKeywordId
import cinescout.tvshows.domain.model.TmdbTvShowId

internal fun DatabaseTmdbGenreId.toId() = TmdbGenreId(value)
internal fun DatabaseTmdbKeywordId.toId() = TmdbKeywordId(value)
internal fun DatabaseTmdbTvShowId.toId() = TmdbTvShowId(value)
internal fun Rating.toDatabaseRating() = value
internal fun TmdbGenreId.toDatabaseId() = DatabaseTmdbGenreId(value)
internal fun TmdbKeywordId.toDatabaseId() = DatabaseTmdbKeywordId(value)
fun TmdbTvShowId.toDatabaseId() = DatabaseTmdbTvShowId(value)
fun TmdbTvShowId.toScreenplayDatabaseId() = DatabaseTmdbTvShowId(value)
