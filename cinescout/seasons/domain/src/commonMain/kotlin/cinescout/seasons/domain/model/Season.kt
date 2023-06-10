package cinescout.seasons.domain.model

import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.SeasonNumber
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
