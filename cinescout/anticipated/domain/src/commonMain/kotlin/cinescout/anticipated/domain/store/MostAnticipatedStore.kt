package cinescout.anticipated.domain.store

import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.Store5

interface MostAnticipatedStore : Store5<MostAnticipatedStore.Key, List<Screenplay>> {

    data class Key(val type: ScreenplayType)
}
