package cinescout.seasons.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.ids.SeasonIds
import korlibs.time.Date

data class Season(
    val episodeCount: Int,
    val firstAirDate: Option<Date>,
    val ids: SeasonIds,
    val number: SeasonNumber,
    val rating: PublicRating,
    val title: String
)
