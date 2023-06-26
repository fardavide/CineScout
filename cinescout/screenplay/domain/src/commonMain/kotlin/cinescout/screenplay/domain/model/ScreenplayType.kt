package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TvShowIds

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
