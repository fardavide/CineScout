package cinescout.seasons.data.remote.mapper

import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.seasons.data.remote.model.TraktEpisodeExtendedBody
import cinescout.seasons.domain.model.Episode
import korlibs.time.DateTime
import org.koin.core.annotation.Factory

@Factory
internal class TraktEpisodeMapper(
    private val idMapper: TraktEpisodeIdMapper
) {

    fun toDomainModel(episode: TraktEpisodeExtendedBody) = Episode(
        // TODO use optional
        firstAirDate = episode.firstAirDate?.date ?: DateTime.EPOCH.date,
        ids = idMapper.toDomainModel(episode.ids),
        number = episode.number,
        overview = episode.overview,
        rating = PublicRating(
            average = Rating.of(episode.voteAverage).getOrThrow(),
            voteCount = episode.voteCount
        ),
        runtime = episode.runtime,
        seasonNumber = episode.seasonNumber,
        title = episode.title
    )
}
