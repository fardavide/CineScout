package cinescout.seasons.data.remote.mapper

import cinescout.screenplay.domain.model.id.SeasonIds
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktSeasonIds

@Factory
internal class TraktSeasonIdMapper {

    fun toDomainModel(ids: TraktSeasonIds) = SeasonIds(
        tmdb = ids.tmdb,
        trakt = ids.trakt
    )
}
