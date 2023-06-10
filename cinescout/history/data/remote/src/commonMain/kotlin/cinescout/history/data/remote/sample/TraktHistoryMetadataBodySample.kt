package cinescout.history.data.remote.sample

import cinescout.history.data.remote.model.TraktEpisodeHistoryMetadataBody
import cinescout.history.domain.sample.HistoryItemIdSample
import cinescout.sample.DateTimeSample
import screenplay.data.remote.trakt.sample.TraktEpisodeMetadataBodySample
import screenplay.data.remote.trakt.sample.TraktScreenplayMetadataBodySample

object TraktHistoryMetadataBodySample {

    val BreakingBad_s1e1_watched = TraktEpisodeHistoryMetadataBody(
        episode = TraktEpisodeMetadataBodySample.BreakingBad_s1e1,
        id = HistoryItemIdSample.BreakingBad_s1e1,
        show = TraktScreenplayMetadataBodySample.BreakingBad,
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s1e2_watched = TraktEpisodeHistoryMetadataBody(
        episode = TraktEpisodeMetadataBodySample.BreakingBad_s1e2,
        id = HistoryItemIdSample.BreakingBad_s1e2,
        show = TraktScreenplayMetadataBodySample.BreakingBad,
        watchedAt = DateTimeSample.Xmas2023
    )

    val BreakingBad_s1e3_watched = TraktEpisodeHistoryMetadataBody(
        episode = TraktEpisodeMetadataBodySample.BreakingBad_s1e3,
        id = HistoryItemIdSample.BreakingBad_s1e3,
        show = TraktScreenplayMetadataBodySample.BreakingBad,
        watchedAt = DateTimeSample.Xmas2023
    )
}
