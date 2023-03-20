package cinescout.suggestions.presentation.sample

import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import cinescout.suggestions.domain.sample.SuggestedScreenplayWithExtrasSample
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import kotlinx.collections.immutable.toImmutableList

object ForYouScreenplayUiModelSample {

    val BreakingBad = ForYouScreenplayUiModel(
        tmdbScreenplayId = ScreenplaySample.BreakingBad.tmdbId,
        actors = ScreenplayCreditsSample.BreakingBad.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedScreenplaySample.BreakingBad.affinity.value,
        genres = SuggestedScreenplayWithExtrasSample.BreakingBad.genres.genres.map { genre -> genre.name }
            .toImmutableList(),
        rating = ScreenplaySample.BreakingBad.rating.average.value.toString(),
        releaseYear = ScreenplaySample.BreakingBad.firstAirDate.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = ScreenplaySample.BreakingBad.title
    )

    val Dexter = ForYouScreenplayUiModel(
        actors = ScreenplayCreditsSample.Dexter.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedScreenplaySample.Dexter.affinity.value,
        genres = SuggestedScreenplayWithExtrasSample.Dexter.genres.genres.map { genre -> genre.name }
            .toImmutableList(),
        rating = ScreenplaySample.Dexter.rating.average.value.toString(),
        releaseYear = ScreenplaySample.Dexter.firstAirDate.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = ScreenplaySample.Dexter.title,
        tmdbScreenplayId = ScreenplaySample.Dexter.tmdbId
    )

    val Grimm = ForYouScreenplayUiModel(
        actors = ScreenplayCreditsSample.Grimm.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedScreenplaySample.Grimm.affinity.value,
        genres = SuggestedScreenplayWithExtrasSample.Grimm.genres.genres.map { genre -> genre.name }
            .toImmutableList(),
        rating = ScreenplaySample.Grimm.rating.average.value.toString(),
        releaseYear = ScreenplaySample.Grimm.firstAirDate.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_suggested),
        title = ScreenplaySample.Grimm.title,
        tmdbScreenplayId = ScreenplaySample.Grimm.tmdbId
    )

    val Inception = ForYouScreenplayUiModel(
        actors = ScreenplayCreditsSample.Inception.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedScreenplaySample.Inception.affinity.value,
        genres = SuggestedScreenplayWithExtrasSample.Inception.genres.genres.map { genre -> genre.name }
            .toImmutableList(),
        rating = ScreenplaySample.Inception.rating.average.value.toString(),
        releaseYear = ScreenplaySample.Inception.releaseDate.orNull()?.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_suggested),
        title = ScreenplaySample.Inception.title,
        tmdbScreenplayId = ScreenplaySample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = ForYouScreenplayUiModel(
        actors = ScreenplayCreditsSample.TheWolfOfWallStreet.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedScreenplaySample.TheWolfOfWallStreet.affinity.value,
        genres = SuggestedScreenplayWithExtrasSample.TheWolfOfWallStreet.genres.genres.map { genre -> genre.name }
            .toImmutableList(),
        rating = ScreenplaySample.TheWolfOfWallStreet.rating.average.value.toString(),
        releaseYear = ScreenplaySample.TheWolfOfWallStreet.releaseDate.orNull()?.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = ScreenplaySample.TheWolfOfWallStreet.title,
        tmdbScreenplayId = ScreenplaySample.TheWolfOfWallStreet.tmdbId
    )

    val War = ForYouScreenplayUiModel(
        actors = ScreenplayCreditsSample.War.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedScreenplaySample.War.affinity.value,
        genres = SuggestedScreenplayWithExtrasSample.War.genres.genres.map { genre -> genre.name }
            .toImmutableList(),
        rating = ScreenplaySample.War.rating.average.value.toString(),
        releaseYear = ScreenplaySample.War.releaseDate.orNull()?.year.toString(),
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = ScreenplaySample.War.title,
        tmdbScreenplayId = ScreenplaySample.War.tmdbId
    )
}
