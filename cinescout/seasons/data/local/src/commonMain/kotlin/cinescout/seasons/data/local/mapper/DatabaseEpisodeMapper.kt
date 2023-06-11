package cinescout.seasons.data.local.mapper

import arrow.core.toOption
import cinescout.database.model.DatabaseEpisode
import cinescout.database.model.DatabaseSeasonWithEpisode
import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.ids.SeasonIds
import cinescout.seasons.domain.model.Episode
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseEpisodeMapper {

    fun toDomainModel(seasonWithEpisode: DatabaseSeasonWithEpisode) = Episode(
        firstAirDate = seasonWithEpisode.episodeFirstAirDate.toOption(),
        ids = toEpisodeIds(
            tmdbId = seasonWithEpisode.episodeTmdbId,
            traktId = seasonWithEpisode.episodeTraktId
        ),
        number = EpisodeNumber(seasonWithEpisode.episodeNumber.toInt()),
        overview = seasonWithEpisode.episodeOverview,
        rating = PublicRating(
            average = Rating.of(seasonWithEpisode.episodeRatingAverage).getOrThrow(),
            voteCount = seasonWithEpisode.episodeRatingCount.toInt()
        ),
        runtime = seasonWithEpisode.episodeRuntime,
        seasonNumber = SeasonNumber(seasonWithEpisode.seasonNumber.toInt()),
        title = seasonWithEpisode.episodeTitle
    )

    fun toDatabaseModel(episode: Episode, seasonIds: SeasonIds) = DatabaseEpisode(
        firstAirDate = episode.firstAirDate.getOrNull(),
        number = episode.number.value,
        overview = episode.overview,
        ratingAverage = episode.rating.average.value,
        ratingCount = episode.rating.voteCount.toLong(),
        runtime = episode.runtime,
        seasonId = seasonIds.toTraktDatabaseId(),
        seasonNumber = episode.seasonNumber.value,
        title = episode.title,
        tmdbId = episode.ids.toTmdbDatabaseId(),
        traktId = episode.ids.toTraktDatabaseId()
    )
}
