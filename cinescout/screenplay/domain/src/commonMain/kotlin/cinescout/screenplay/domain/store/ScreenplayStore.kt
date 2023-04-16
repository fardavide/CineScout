package cinescout.screenplay.domain.store

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TraktScreenplayId
import cinescout.store5.Store5

interface ScreenplayStore : Store5<TraktScreenplayId, Screenplay>
