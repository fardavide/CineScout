package cinescout.screenplay.data.local.mapper

import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.model.DatabaseTraktMovieId
import cinescout.database.model.DatabaseTraktScreenplayId
import cinescout.database.model.DatabaseTraktTvShowId
import cinescout.screenplay.domain.model.ScreenplayIds
import org.koin.core.annotation.Factory

@Factory
class DatabaseScreenplayIdsMapper {

    fun toScreenplayIds(traktId: DatabaseTraktScreenplayId, tmdbId: DatabaseTmdbScreenplayId): ScreenplayIds =
        when (traktId) {
            is DatabaseTraktMovieId -> ScreenplayIds.Movie(
                trakt = traktId.toMovieDomainId(),
                tmdb = tmdbId.toMovieDomainId()
            )
            is DatabaseTraktTvShowId -> ScreenplayIds.TvShow(
                trakt = traktId.toTvShowDomainId(),
                tmdb = tmdbId.toTvShowDomainId()
            )
        }
}
