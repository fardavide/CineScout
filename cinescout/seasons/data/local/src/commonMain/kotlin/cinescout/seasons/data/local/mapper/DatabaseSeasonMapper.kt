package cinescout.seasons.data.local.mapper

import arrow.core.Nel
import arrow.core.toOption
import cinescout.database.model.DatabaseSeason
import cinescout.database.model.DatabaseSeasonWithEpisode
import cinescout.screenplay.data.local.mapper.toTmdbDatabaseId
import cinescout.screenplay.data.local.mapper.toTraktDatabaseId
import cinescout.screenplay.data.local.mapper.toTvShowIds
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.seasons.domain.model.Season
import cinescout.seasons.domain.model.SeasonWithEpisodes
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.utils.kotlin.nonEmptyUnsafe
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseSeasonMapper(
    private val episodeMapper: DatabaseEpisodeMapper
) {

    fun toSeasonsWithEpisodes(databaseSeasons: Nel<DatabaseSeasonWithEpisode>): TvShowSeasonsWithEpisodes {
        val seasons = databaseSeasons.groupBy { it.seasonNumber }.map { (_, seasonWithEpisodes) ->
            val databaseSeason = seasonWithEpisodes.first()
            val season = Season(
                episodeCount = databaseSeason.episodeCount.toInt(),
                firstAirDate = databaseSeason.seasonFirstAirDate.toOption(),
                ids = toSeasonIds(
                    tmdbId = databaseSeason.seasonTmdbId,
                    traktId = databaseSeason.seasonTraktId
                ),
                number = SeasonNumber(databaseSeason.seasonNumber),
                rating = PublicRating(
                    average = Rating.of(databaseSeason.seasonRatingAverage).getOrThrow(),
                    voteCount = databaseSeason.seasonRatingCount.toInt()
                ),
                title = databaseSeason.seasonTitle
            )

            val episodes = seasonWithEpisodes.map(episodeMapper::toDomainModel).nonEmptyUnsafe()
            SeasonWithEpisodes(
                season = season,
                episodes = episodes
            )
        }
        return TvShowSeasonsWithEpisodes(
            tvShowIds = toTvShowIds(
                tmdbId = databaseSeasons.first().tmdbTvShowId,
                traktId = databaseSeasons.first().traktTvShowId
            ),
            seasonsWithEpisodes = seasons.nonEmptyUnsafe()
        )
    }

    fun toDatabaseModel(season: Season, tvShowIds: TvShowIds) = DatabaseSeason(
        episodeCount = season.episodeCount.toLong(),
        firstAirDate = season.firstAirDate.getOrNull(),
        number = season.number.value,
        ratingAverage = season.rating.average.value,
        ratingCount = season.rating.voteCount.toLong(),
        title = season.title,
        tmdbId = season.ids.toTmdbDatabaseId(),
        traktId = season.ids.toTraktDatabaseId(),
        tmdbTvShowId = tvShowIds.toTmdbDatabaseId(),
        traktTvShowId = tvShowIds.toTraktDatabaseId()
    )
}
