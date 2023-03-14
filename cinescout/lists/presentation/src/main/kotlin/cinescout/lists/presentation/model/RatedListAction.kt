package cinescout.lists.presentation.model

import cinescout.screenplay.domain.model.ScreenplayType

sealed interface RatedListAction {

    class SelectListType(val listType: ScreenplayType) : RatedListAction
}
