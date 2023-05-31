package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId

sealed interface TraktScreenplayIds {

    val tmdb: TmdbScreenplayId

    val trakt: TraktScreenplayId

    val ids: ScreenplayIds
        get() = ScreenplayIds(tmdb, trakt)
}
