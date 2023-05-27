package cinescout.suggestions.presentation.sample

import arrow.core.getOrElse
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.sample.SuggestedScreenplaySample
import cinescout.suggestions.domain.sample.SuggestedScreenplayWithExtrasSample
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.utils.kotlin.format
import kotlinx.collections.immutable.toImmutableList

object ForYouScreenplayUiModelSample {

    val BreakingBad = ForYouScreenplayUiModel(
        actors = ScreenplayCreditsSample.BreakingBad.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouScreenplayUiModel.Actor(imageUrl.orNull().orEmpty())
        }.toImmutableList(),
        affinity = SuggestedScreenplaySample.BreakingBad.affinity.value,
        genres = ScreenplayGenresSample.BreakingBad.genres.map { genre -> genre.name }
            .toImmutableList(),
        rating = ScreenplaySample.BreakingBad.rating.average.value.format(digits = 1),
        releaseDate = with(ScreenplaySample.BreakingBad.firstAirDate) {
            "${month.localName} $year"
        },
        screenplayIds = ScreenplaySample.BreakingBad.ids,
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
        rating = ScreenplaySample.Dexter.rating.average.value.format(digits = 1),
        releaseDate = with(ScreenplaySample.Dexter.firstAirDate) {
            "${month.localName} $year"
        },
        screenplayIds = ScreenplaySample.Dexter.ids,
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = ScreenplaySample.Dexter.title
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
        rating = ScreenplaySample.Grimm.rating.average.value.format(digits = 1),
        releaseDate = with(ScreenplaySample.Grimm.firstAirDate) { "${month.localName} $year" },
        screenplayIds = ScreenplaySample.Grimm.ids,
        suggestionSource = TextRes(string.suggestions_source_suggested),
        title = ScreenplaySample.Grimm.title
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
        rating = ScreenplaySample.Inception.rating.average.value.format(digits = 1),
        releaseDate = ScreenplaySample.Inception.releaseDate
            .map { "${it.month.localName} ${it.year}" }
            .getOrElse { "" },
        screenplayIds = ScreenplaySample.Inception.ids,
        suggestionSource = TextRes(string.suggestions_source_suggested),
        title = ScreenplaySample.Inception.title
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
        rating = ScreenplaySample.TheWolfOfWallStreet.rating.average.value.format(digits = 1),
        releaseDate = ScreenplaySample.TheWolfOfWallStreet.releaseDate
            .map { "${it.month.localName} ${it.year}" }
            .getOrElse { "" },
        screenplayIds = ScreenplaySample.TheWolfOfWallStreet.ids,
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = ScreenplaySample.TheWolfOfWallStreet.title
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
        rating = ScreenplaySample.War.rating.average.value.format(digits = 1),
        releaseDate = ScreenplaySample.War.releaseDate
            .map { "${it.month.localName} ${it.year}" }
            .getOrElse { "" },
        screenplayIds = ScreenplaySample.War.ids,
        suggestionSource = TextRes(string.suggestions_source_popular),
        title = ScreenplaySample.War.title
    )
}
