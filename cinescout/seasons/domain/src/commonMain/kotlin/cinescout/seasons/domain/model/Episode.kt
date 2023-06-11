package cinescout.seasons.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.ids.EpisodeIds
import korlibs.time.Date
import kotlin.time.Duration

data class Episode(
    val firstAirDate: Option<Date>,
    val ids: EpisodeIds,
    val number: EpisodeNumber,
    val overview: String,
    val rating: PublicRating,
    val runtime: Duration,
    val seasonNumber: SeasonNumber,
    val title: String
)
