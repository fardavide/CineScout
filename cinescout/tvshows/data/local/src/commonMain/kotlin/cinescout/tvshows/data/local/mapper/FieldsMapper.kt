package cinescout.tvshows.data.local.mapper

import cinescout.common.model.Rating
import cinescout.common.model.TmdbGenreId
import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.tvshows.domain.model.TmdbTvShowId

internal fun DatabaseTmdbGenreId.toId() = TmdbGenreId(value)
internal fun DatabaseTmdbTvShowId.toId() = TmdbTvShowId(value)
internal fun Rating.toDatabaseRating() = value
internal fun TmdbGenreId.toDatabaseId() = DatabaseTmdbGenreId(value)
internal fun TmdbTvShowId.toDatabaseId() = DatabaseTmdbTvShowId(value)
