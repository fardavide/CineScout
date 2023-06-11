package cinescout.seasons.data.remote.mapper

import arrow.core.toOption
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.seasons.data.remote.model.TraktSeasonsExtendedWithEpisodesResponse
import cinescout.seasons.domain.model.Season
import cinescout.seasons.domain.model.SeasonWithEpisodes
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.utils.kotlin.nonEmptyUnsafe
import org.koin.core.annotation.Factory

@Factory
internal class TraktSeasonMapper(
    private val episodeMapper: TraktEpisodeMapper,
    private val seasonIdMapper: TraktSeasonIdMapper
) {

    fun toDomainModel(tvShowIds: TvShowIds, response: TraktSeasonsExtendedWithEpisodesResponse) =
        TvShowSeasonsWithEpisodes(
            seasonsWithEpisodes = response.map { season ->
                SeasonWithEpisodes(
                    season = Season(
                        episodeCount = season.episodeCount,
                        firstAirDate = season.firstAirDate?.date.toOption(),
                        ids = seasonIdMapper.toDomainModel(season.ids),
                        number = season.number,
                        rating = PublicRating(
                            average = Rating.of(season.voteAverage).getOrThrow(),
                            voteCount = season.voteCount
                        ),
                        title = season.title
                    ),
                    episodes = season.episodes.map(episodeMapper::toDomainModel)
                )
            }.nonEmptyUnsafe(),
            tvShowIds = tvShowIds
        )
}
