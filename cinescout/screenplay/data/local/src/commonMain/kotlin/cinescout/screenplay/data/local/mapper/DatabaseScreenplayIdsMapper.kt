package cinescout.screenplay.data.local.mapper

import cinescout.database.model.id.DatabaseTmdbScreenplayId
import cinescout.database.model.id.DatabaseTraktMovieId
import cinescout.database.model.id.DatabaseTraktScreenplayId
import cinescout.database.model.id.DatabaseTraktTvShowId
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds
import org.koin.core.annotation.Factory

@Factory
class DatabaseScreenplayIdsMapper {

    fun toScreenplayIds(traktId: DatabaseTraktScreenplayId, tmdbId: DatabaseTmdbScreenplayId): ScreenplayIds =
        when (traktId) {
            is DatabaseTraktMovieId -> MovieIds(
                trakt = traktId.toMovieDomainId(),
                tmdb = tmdbId.toMovieDomainId()
            )
            is DatabaseTraktTvShowId -> TvShowIds(
                trakt = traktId.toTvShowDomainId(),
                tmdb = tmdbId.toTvShowDomainId()
            )
        }
}
