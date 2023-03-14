package cinescout.lists.presentation.model

import cinescout.screenplay.domain.model.ScreenplayType

sealed interface DislikedListAction {

    class SelectListType(val listType: ScreenplayType) : DislikedListAction
}
