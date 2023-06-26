package cinescout.seasons.data.remote.mapper

import cinescout.screenplay.domain.model.id.EpisodeIds
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktEpisodeIds

@Factory
internal class TraktEpisodeIdMapper {

    fun toDomainModel(ids: TraktEpisodeIds) = EpisodeIds(
        tmdb = ids.tmdb,
        trakt = ids.trakt
    )
}
