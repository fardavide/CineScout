package cinescout.screenplay.domain.store

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.store5.Store5

interface SimilarScreenplaysStore : Store5<ScreenplayIds, List<Screenplay>>
