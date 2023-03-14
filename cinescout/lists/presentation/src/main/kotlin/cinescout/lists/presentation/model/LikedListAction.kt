package cinescout.lists.presentation.model

import cinescout.screenplay.domain.model.ScreenplayType

sealed interface LikedListAction {

    class SelectListType(val listType: ScreenplayType) : LikedListAction
}
