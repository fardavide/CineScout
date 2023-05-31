package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds

enum class ScreenplayType {

    Movie,
    TvShow;

    companion object {
        fun from(ids: ScreenplayIds) = when (ids) {
            is MovieIds -> Movie
            is TvShowIds -> TvShow
        }
    }
}
