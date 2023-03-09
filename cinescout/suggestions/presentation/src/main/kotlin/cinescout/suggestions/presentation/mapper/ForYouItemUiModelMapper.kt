package cinescout.suggestions.presentation.mapper

import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.screenplay.domain.model.TmdbProfileImage
import cinescout.suggestions.domain.model.SuggestedMovieWithExtras
import cinescout.suggestions.domain.model.SuggestedTvShowWithExtras
import cinescout.suggestions.domain.model.SuggestionSource
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
            affinity = suggestedMovieWithExtras.affinity.value,
            backdropUrl = movie.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = suggestedMovieWithExtras.genres.map { genre -> genre.name }.toImmutableList(),
            posterUrl = movie.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = movie.rating.average.value.toString(),
            releaseYear = movie.releaseDate.orNull()?.year?.toString().orEmpty(),
            suggestionSource = toSourceTextRes(suggestedMovieWithExtras.source),
            title = movie.title,
            tmdbScreenplayId = movie.tmdbId
        )
    }

    override fun toUiModel(suggestedTvShowWithExtras: SuggestedTvShowWithExtras): ForYouScreenplayUiModel {
        val credits = suggestedTvShowWithExtras.credits
        val tvShow = suggestedTvShowWithExtras.tvShow
        return ForYouScreenplayUiModel(
            actors = toTvShowActorsUiModels(credits.cast).toImmutableList(),
            affinity = suggestedTvShowWithExtras.affinity.value,
            backdropUrl = tvShow.backdropImage.orNull()?.getUrl(TmdbBackdropImage.Size.ORIGINAL),
            genres = suggestedTvShowWithExtras.genres.map { genre -> genre.name }.toImmutableList(),
            posterUrl = tvShow.posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM),
            rating = tvShow.rating.average.value.toString(),
            releaseYear = tvShow.firstAirDate.year.toString(),
            suggestionSource = toSourceTextRes(suggestedTvShowWithExtras.source),
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

    private fun toSourceTextRes(source: SuggestionSource): TextRes = when (source) {
        is SuggestionSource.FromLiked -> TextRes(string.suggestions_source_liked, source.title)
        is SuggestionSource.FromRated -> TextRes(string.suggestions_source_liked, source.title)
        is SuggestionSource.FromWatchlist -> TextRes(string.suggestions_source_liked, source.title)
        is SuggestionSource.PersonalSuggestions -> TextRes(string.suggestions_source_personal_suggestion)
        SuggestionSource.Popular -> TextRes(string.suggestions_source_popular)
        SuggestionSource.Suggested -> TextRes(string.suggestions_source_suggested)
        SuggestionSource.Trending -> TextRes(string.suggestions_source_trending)
        SuggestionSource.Upcoming -> TextRes(string.suggestions_source_upcoming)
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
