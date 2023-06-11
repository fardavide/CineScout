package cinescout.seasons.data.remote.mapper

import arrow.core.toOption
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.seasons.data.remote.model.TraktEpisodeExtendedBody
import cinescout.seasons.domain.model.Episode
import org.koin.core.annotation.Factory

@Factory
internal class TraktEpisodeMapper(
    private val idMapper: TraktEpisodeIdMapper
) {

    fun toDomainModel(episode: TraktEpisodeExtendedBody) = Episode(
        firstAirDate = episode.firstAirDate?.date.toOption(),
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
