package cinescout.lists.domain

import cinescout.screenplay.domain.model.ScreenplayType

@Deprecated(
    "Use ScreenplayType instead",
    ReplaceWith("ScreenplayType", "cinescout.screenplay.domain.model.ScreenplayType")
)
typealias ListType = ScreenplayType
