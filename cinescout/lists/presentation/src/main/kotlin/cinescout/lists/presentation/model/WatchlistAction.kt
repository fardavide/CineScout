package cinescout.lists.presentation.model

import cinescout.screenplay.domain.model.ScreenplayType

sealed interface WatchlistAction {

    class SelectListType(val listType: ScreenplayType) : WatchlistAction
}
