package cinescout.people.domain.store

import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5

interface ScreenplayCreditsStore : Store5<TmdbScreenplayId, ScreenplayCredits>
