package cinescout.screenplay.domain.store

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5

interface SimilarScreenplaysStore : Store5<TmdbScreenplayId, List<Screenplay>>
