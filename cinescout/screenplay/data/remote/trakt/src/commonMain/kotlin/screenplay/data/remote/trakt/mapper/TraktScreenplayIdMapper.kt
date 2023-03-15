package screenplay.data.remote.trakt.mapper

import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktScreenplaysMetadataResponse

@Factory
class TraktScreenplayIdMapper {

    fun toScreenplayIds(ids: TraktScreenplaysMetadataResponse): List<TmdbScreenplayId> =
        ids.map { metadataBody -> metadataBody.tmdbId }
}
