package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.ids.EpisodeIds
import korlibs.time.Date
import kotlin.time.Duration

data class Episode(
    val firstAirDate: Date,
    val ids: EpisodeIds,
    val number: EpisodeNumber,
    val overview: String,
    val rating: PublicRating,
    val runtime: Duration,
    val seasonNumber: SeasonNumber,
    val title: String
)
