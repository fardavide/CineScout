package cinescout.seasons.domain.model

import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.ids.SeasonIds
import korlibs.time.Date

data class Season(
    val episodeCount: Int,
    val firstAirDate: Date,
    val ids: SeasonIds,
    val number: SeasonNumber,
    val rating: PublicRating,
    val title: String
)
