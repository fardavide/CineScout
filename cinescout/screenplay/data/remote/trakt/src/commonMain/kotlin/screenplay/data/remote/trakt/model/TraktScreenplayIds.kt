package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TraktScreenplayId

sealed interface TraktScreenplayIds {

    val tmdb: TmdbScreenplayId

    val trakt: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdb, trakt)
}
