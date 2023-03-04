package cinescout.suggestions.presentation.mapper

import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import cinescout.suggestions.domain.model.SuggestedMovieWithExtras
import cinescout.suggestions.domain.model.SuggestedTvShowWithExtras
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.sample.ForYouScreenplayUiModelSample
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

interface ForYouItemUiModelMapper {

    fun toUiModel(suggestedMovieWithExtras: SuggestedMovieWithExtras): ForYouScreenplayUiModel

    fun toUiModel(suggestedTvShowWithExtras: SuggestedTvShowWithExtras): ForYouScreenplayUiModel

}

@Factory
class RealForYouItemUiModelMapper : ForYouItemUiModelMapper {

    override fun toUiModel(suggestedMovieWithExtras: SuggestedMovieWithExtras): ForYouScreenplayUiModel {
        val credits = suggestedMovieWithExtras.credits
        val movie = suggestedMovieWithExtras.movie
        return ForYouScreenplayUiModel(
            actors = toMovieActorsUiModels(credits.cast).toImmutableList(),
            backdropUrl = movie.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = suggestedMovieWithExtras.genres.map { genre -> genre.name }.toImmutableList(),
            posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = movie.rating.average.value.toString(),
            releaseYear = movie.releaseDate.orNull()?.year?.toString().orEmpty(),
            title = movie.title,
            tmdbScreenplayId = movie.tmdbId
        )
    }

    override fun toUiModel(suggestedTvShowWithExtras: SuggestedTvShowWithExtras): ForYouScreenplayUiModel {
        val credits = suggestedTvShowWithExtras.credits
        val tvShow = suggestedTvShowWithExtras.tvShow
        return ForYouScreenplayUiModel(
            actors = toTvShowActorsUiModels(credits.cast).toImmutableList(),
            backdropUrl = tvShow.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = suggestedTvShowWithExtras.genres.map { genre -> genre.name }.toImmutableList(),
            posterUrl = tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = tvShow.rating.average.value.toString(),
            releaseYear = tvShow.firstAirDate.year.toString(),
            title = tvShow.title,
            tmdbScreenplayId = tvShow.tmdbId
        )
    }

    private fun toMovieActorsUiModels(actors: List<CastMember>): List<ForYouScreenplayUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouScreenplayUiModel.Actor(profileImageUrl = profileImageUrl)
        }

    private fun toTvShowActorsUiModels(actors: List<CastMember>): List<ForYouScreenplayUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouScreenplayUiModel.Actor(profileImageUrl = profileImageUrl)
        }
}

class FakeForYouItemUiModelMapper(
    private val movieForYouScreenplayUiModel: ForYouScreenplayUiModel = ForYouScreenplayUiModelSample.Inception,
    private val tvShowForYouScreenplayUiModel: ForYouScreenplayUiModel = ForYouScreenplayUiModelSample.BreakingBad
) : ForYouItemUiModelMapper {

    override fun toUiModel(suggestedMovieWithExtras: SuggestedMovieWithExtras): ForYouScreenplayUiModel =
        movieForYouScreenplayUiModel

    override fun toUiModel(suggestedTvShowWithExtras: SuggestedTvShowWithExtras): ForYouScreenplayUiModel =
        tvShowForYouScreenplayUiModel
}
