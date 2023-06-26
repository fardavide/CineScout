package cinescout.network.trakt.model

import cinescout.screenplay.domain.model.id.ScreenplayIds

fun ScreenplayIds.toTraktIdQueryString() = trakt.value.toString()
