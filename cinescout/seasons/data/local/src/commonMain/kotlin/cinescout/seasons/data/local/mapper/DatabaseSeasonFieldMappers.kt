package cinescout.seasons.data.local.mapper

import cinescout.database.model.id.DatabaseTmdbEpisodeId
import cinescout.database.model.id.DatabaseTmdbSeasonId
import cinescout.database.model.id.DatabaseTraktEpisodeId
import cinescout.database.model.id.DatabaseTraktSeasonId
import cinescout.screenplay.domain.model.ids.EpisodeIds
import cinescout.screenplay.domain.model.ids.SeasonIds
import cinescout.screenplay.domain.model.ids.TmdbEpisodeId
import cinescout.screenplay.domain.model.ids.TmdbSeasonId
import cinescout.screenplay.domain.model.ids.TraktEpisodeId
import cinescout.screenplay.domain.model.ids.TraktSeasonId

fun EpisodeIds.toTmdbDatabaseId() = DatabaseTmdbEpisodeId(tmdb.value)
fun EpisodeIds.toTraktDatabaseId() = DatabaseTraktEpisodeId(trakt.value)
fun SeasonIds.toTmdbDatabaseId() = DatabaseTmdbSeasonId(tmdb.value)
fun SeasonIds.toTraktDatabaseId() = DatabaseTraktSeasonId(trakt.value)

fun toEpisodeIds(tmdbId: DatabaseTmdbEpisodeId, traktId: DatabaseTraktEpisodeId) = EpisodeIds(
    tmdb = tmdbId.toTmdbId(),
    trakt = traktId.toTraktId()
)
fun DatabaseTmdbEpisodeId.toTmdbId() = TmdbEpisodeId(value)
fun DatabaseTraktEpisodeId.toTraktId() = TraktEpisodeId(value)
fun toSeasonIds(tmdbId: DatabaseTmdbSeasonId, traktId: DatabaseTraktSeasonId) = SeasonIds(
    tmdb = tmdbId.toTmdbId(),
    trakt = traktId.toTraktId()
)
fun DatabaseTmdbSeasonId.toTmdbId() = TmdbSeasonId(value)
fun DatabaseTraktSeasonId.toTraktId() = TraktSeasonId(value)
