package cinescout.network.trakt.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds

fun ScreenplayIds.toTraktIdQueryString() = trakt.value.toString()
