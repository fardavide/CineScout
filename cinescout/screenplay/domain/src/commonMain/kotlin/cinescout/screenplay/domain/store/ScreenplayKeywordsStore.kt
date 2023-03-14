package cinescout.screenplay.domain.store

import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5

interface ScreenplayKeywordsStore : Store5<TmdbScreenplayId, ScreenplayKeywords>
