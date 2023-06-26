package cinescout.history.domain.model

import cinescout.screenplay.domain.model.SeasonAndEpisodeNumber
import cinescout.screenplay.domain.model.id.ContentIds
import cinescout.screenplay.domain.model.id.EpisodeIds
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.SeasonIds
import cinescout.screenplay.domain.model.id.TvShowIds

sealed interface HistoryStoreKey {

    data class Read(val screenplayIds: ScreenplayIds) : HistoryStoreKey

    sealed interface Write : HistoryStoreKey {

        sealed interface Add : Write {

            val contentIds: ContentIds

            data class Episode(
                val episodeIds: EpisodeIds,
                val tvShowIds: TvShowIds,
                val episode: SeasonAndEpisodeNumber
            ) : Add {

                override val contentIds = episodeIds
            }

            data class Movie(val movieIds: MovieIds) : Add {

                override val contentIds = movieIds
            }

            data class Season(
                val seasonIds: SeasonIds,
                val tvShowIds: TvShowIds,
                val episodes: List<SeasonAndEpisodeNumber>
            ) : Add {

                override val contentIds = seasonIds
            }

            data class TvShow(
                val tvShowIds: TvShowIds,
                val episodes: List<SeasonAndEpisodeNumber>
            ) : Add {

                override val contentIds = tvShowIds
            }
        }

        data class Delete(val screenplayId: ScreenplayIds) : Write
    }
}
