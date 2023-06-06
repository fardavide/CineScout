package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.ids.SeasonIds
import korlibs.time.Date

data class Season(
    val firstAirDate: Date,
    val episodeCount: Int,
    val ids: SeasonIds,
    val number: SeasonNumber,
    val rating: PublicRating,
    val title: String
)
