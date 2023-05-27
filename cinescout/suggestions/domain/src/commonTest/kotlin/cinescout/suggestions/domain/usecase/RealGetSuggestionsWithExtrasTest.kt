package cinescout.suggestions.domain.usecase

import app.cash.turbine.test
import arrow.core.Nel
import arrow.core.Option
import arrow.core.nonEmptyListOf
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.usecase.FakeGetExtra
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.media.domain.sample.ScreenplayMediaSample
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.model.PersonalRating
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.store.FakeScreenplayStore
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.sample.SuggestedScreenplayIdSample
import cinescout.watchlist.domain.model.IsInWatchlist
import cinescout.watchlist.domain.sample.ScreenplayWatchlistSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class RealGetSuggestionsWithExtrasTest : BehaviorSpec({

    Given("use case") {
        val screenplayId = SuggestedScreenplayIdSample.Inception

        When("called with all the extras") {
            val scenario = TestScenario(
                screenplay = ScreenplaySample.Inception,
                credits = ScreenplayCreditsSample.Inception,
                genres = ScreenplayGenresSample.Inception,
                keywords = ScreenplayKeywordsSample.Inception,
                media = ScreenplayMediaSample.Inception,
                personalRating = ScreenplayPersonalRatingSample.Inception.toOption(),
                isInWatchlist = ScreenplayWatchlistSample.Inception,
                suggestionIds = nonEmptyListOf(screenplayId)
            )
            val result = scenario.sut(
                type = ScreenplayTypeFilter.All,
                refresh = true,
                refreshExtras = true,
                WithCredits,
                WithGenres,
                WithPersonalRating
            )

            Then("the screenplay with extras is returned") {
                result.test {
                    val items = awaitItem().getOrNull().shouldNotBeNull()
                    with(items.first()) {
                        screenplay shouldBe ScreenplaySample.Inception
                        credits shouldBe ScreenplayCreditsSample.Inception
                        genres shouldBe ScreenplayGenresSample.Inception
                        personalRating shouldBe ScreenplayPersonalRatingSample.Inception.toOption()
                    }
                    awaitComplete()
                }
            }
        }
    }
})

private class RealGetSuggestionsWithExtrasTestScenario(
    val sut: RealGetSuggestionsWithExtras
)

private fun TestScenario(
    credits: ScreenplayCredits,
    genres: ScreenplayGenres,
    isInWatchlist: Boolean,
    keywords: ScreenplayKeywords,
    media: ScreenplayMedia,
    personalRating: Option<Rating>,
    screenplay: Screenplay,
    suggestionIds: Nel<SuggestedScreenplayId>
) = RealGetSuggestionsWithExtrasTestScenario(
    sut = RealGetSuggestionsWithExtras(
        getExtra = FakeGetExtra(
            credits = credits,
            genres = genres,
            keywords = keywords,
            media = media,
            personalRating = PersonalRating(personalRating),
            isInWatchlist = IsInWatchlist(isInWatchlist)
        ),
        getSuggestionIds = FakeGetSuggestionIds(suggestions = suggestionIds),
        screenplayStore = FakeScreenplayStore(screenplay)
    )
)
