package cinescout.screenplay.domain.store

import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5

interface ScreenplayGenresStore : Store5<TmdbScreenplayId, ScreenplayGenres>
