package cinescout.progress.domain.usecase

import arrow.core.Nel
import cinescout.CineScoutTestApi
import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistoryItem
import cinescout.history.domain.model.TvShowHistory
import cinescout.model.Percent
import cinescout.model.percent
import cinescout.progress.domain.model.EpisodeProgress
import cinescout.progress.domain.model.MovieProgress
import cinescout.progress.domain.model.SeasonProgress
import cinescout.progress.domain.model.TvShowProgress
import cinescout.progress.domain.sample.ScreenplayProgressSample
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.TvShow
import cinescout.seasons.domain.model.Season
import cinescout.seasons.domain.model.SeasonWithEpisodes
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.utils.kotlin.ComputationDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

interface CalculateProgress {

    suspend operator fun invoke(movie: Movie, history: MovieHistory): MovieProgress

    suspend operator fun invoke(
        tvShow: TvShow,
        seasons: TvShowSeasonsWithEpisodes,
        history: TvShowHistory
    ): TvShowProgress
}

@Factory
internal class RealCalculateProgress(
    @Named(ComputationDispatcher) private val dispatcher: CoroutineDispatcher
) : CalculateProgress {

    override suspend operator fun invoke(movie: Movie, history: MovieHistory): MovieProgress =
        withContext(dispatcher) {
            check(movie.ids == history.screenplayIds) { "Movie and history must have the same ids" }

            when (history.items.isEmpty()) {
                true -> MovieProgress.Unwatched(movie)
                false -> MovieProgress.Watched(
                    screenplay = movie,
                    count = history.items.size
                )
            }
        }

    override suspend operator fun invoke(
        tvShow: TvShow,
        seasons: TvShowSeasonsWithEpisodes,
        history: TvShowHistory
    ): TvShowProgress = withContext(dispatcher) {
        check(tvShow.ids == history.screenplayIds) { "TvShow and history must have the same ids" }

        val mutableHistoryItems = history.items.toMutableList()

        val seasonProgresses = seasons.seasonsWithEpisodes.map { seasonWithEpisodes ->
            val season = seasonWithEpisodes.season
            val seasonHistoryItems = mutableHistoryItems.filter { it.seasonNumber == season.number }
            mutableHistoryItems.removeAll(seasonHistoryItems)

            val episodesProgress = seasonWithEpisodes.episodes.map { episode ->
                val episodeHistoryItems = seasonHistoryItems.filter { it.episodeNumber == episode.number }
                when (val episodeHistoryItemsCount = episodeHistoryItems.size) {
                    0 -> EpisodeProgress.Unwatched(episode)
                    else -> EpisodeProgress.Watched(episode = episode, count = episodeHistoryItemsCount)
                }
            }

            @Suppress("UNCHECKED_CAST")
            when {
                episodesProgress.all { it is EpisodeProgress.Unwatched } -> SeasonProgress.Unwatched(
                    season = season,
                    episodesProgress = episodesProgress as List<EpisodeProgress.Unwatched>
                )
                episodesProgress.all { it is EpisodeProgress.Watched } -> SeasonProgress.Completed(
                    season = season,
                    episodesProgress = episodesProgress as List<EpisodeProgress.Watched>
                )
                else -> SeasonProgress.InProgress(
                    season = season,
                    episodesProgress = episodesProgress,
                    progress = Percent.of(
                        episodesProgress.distinctBy { it.episode }.size,
                        season.episodeCount
                    )
                )
            }
        }

        val seasonsWithoutSpecial = seasons.seasonsWithEpisodes.filterNot(::isSpecial)
        val seasonsProgressWithoutSpecials = seasonProgresses.filterNot(::isSpecial)

        @Suppress("UNCHECKED_CAST")
        when {
            seasonProgresses.all { it is SeasonProgress.Unwatched } -> TvShowProgress.Unwatched(
                screenplay = tvShow,
                seasonsProgress = seasonProgresses as Nel<SeasonProgress.Unwatched>
            )

            seasonsProgressWithoutSpecials.all { it is SeasonProgress.Completed } -> TvShowProgress.Completed(
                screenplay = tvShow,
                seasonsProgress = seasonProgresses
            )

            else -> TvShowProgress.InProgress(
                screenplay = tvShow,
                seasonsProgress = seasonProgresses,
                progress = Percent.of(
                    value = history.items.filterNot(::isSpecial)
                        .distinctBy { it.seasonNumber to it.episodeNumber }
                        .size,
                    total = seasonsWithoutSpecial.sumOf { it.episodes.size }
                )
            )
        }
    }

    private fun isSpecial(historyItem: ScreenplayHistoryItem.Episode) = isSpecial(historyItem.seasonNumber)
    private fun isSpecial(seasonWithEpisodes: SeasonWithEpisodes) = isSpecial(seasonWithEpisodes.season)
    private fun isSpecial(seasonProgress: SeasonProgress) = isSpecial(seasonProgress.season)
    private fun isSpecial(season: Season) = isSpecial(season.number)
    private fun isSpecial(seasonNumber: SeasonNumber) = seasonNumber == SeasonNumber(0)

    private fun SeasonProgress.percent(): Percent = when (this) {
        is SeasonProgress.Unwatched -> 0.percent
        is SeasonProgress.Completed, is SeasonProgress.WaitingForNextEpisode -> 100.percent
        is SeasonProgress.InProgress -> progress
    }
}

@CineScoutTestApi
class FakeCalculateProgress(
    private val movieProgress: MovieProgress = ScreenplayProgressSample.Inception_Unwatched,
    private val tvShowProgress: TvShowProgress = ScreenplayProgressSample.BreakingBad_Unwatched
) : CalculateProgress {

    override suspend fun invoke(movie: Movie, history: MovieHistory): MovieProgress = movieProgress

    override suspend fun invoke(
        tvShow: TvShow,
        seasons: TvShowSeasonsWithEpisodes,
        history: TvShowHistory
    ): TvShowProgress = tvShowProgress
}
