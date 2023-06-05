package cinescout.details.domain.usecase

import app.cash.turbine.test
import arrow.core.Option
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithKeywords
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithWatchlist
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
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.store.FakeScreenplayStore
import cinescout.watchlist.domain.model.IsInWatchlist
import cinescout.watchlist.domain.sample.ScreenplayWatchlistSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class RealGetScreenplayWithExtrasTest : BehaviorSpec({

    Given("use case") {
        val screenplayId = ScreenplayIdsSample.Inception

        When("called with all the extras") {
            val scenario = TestScenario(
                screenplay = ScreenplaySample.Inception,
                credits = ScreenplayCreditsSample.Inception,
                genres = ScreenplayGenresSample.Inception,
                keywords = ScreenplayKeywordsSample.Inception,
                media = ScreenplayMediaSample.Inception,
                personalRating = ScreenplayPersonalRatingSample.Inception.toOption(),
                isInWatchlist = ScreenplayWatchlistSample.Inception
            )
            val result = scenario.sut(
                screenplayIds = screenplayId,
                refresh = true,
                refreshExtras = true,
                WithCredits,
                WithGenres,
                WithKeywords,
                WithMedia,
                WithPersonalRating,
                WithWatchlist
            )

            Then("the screenplay with extras is emitted") {
                result.test {
                    val item = awaitItem().getOrNull().shouldNotBeNull()
                    with(item) {
                        screenplay shouldBe ScreenplaySample.Inception
                        credits shouldBe ScreenplayCreditsSample.Inception
                        genres shouldBe ScreenplayGenresSample.Inception
                        keywords shouldBe ScreenplayKeywordsSample.Inception
                        media shouldBe ScreenplayMediaSample.Inception
                        personalRating shouldBe ScreenplayPersonalRatingSample.Inception.toOption()
                        isInWatchlist shouldBe ScreenplayWatchlistSample.Inception
                    }
                    awaitComplete()
                }
            }
        }
    }

    Given("a screenplay with extras") {
        val screenplayId = ScreenplayIdsSample.Inception
        val isInWatchlistFlow = MutableStateFlow(false)

        When("watchlist is updated") {
            val scenario = TestScenario(
                screenplay = ScreenplaySample.Inception,
                credits = ScreenplayCreditsSample.Inception,
                genres = ScreenplayGenresSample.Inception,
                keywords = ScreenplayKeywordsSample.Inception,
                media = ScreenplayMediaSample.Inception,
                personalRating = ScreenplayPersonalRatingSample.Inception.toOption(),
                isInWatchlistFlow = isInWatchlistFlow
            )
            val result = scenario.sut(
                screenplayIds = screenplayId,
                refresh = false,
                refreshExtras = false,
                WithWatchlist
            )

            result.test {
                with(awaitItem().getOrNull().shouldNotBeNull()) {
                    screenplay shouldBe ScreenplaySample.Inception
                    isInWatchlist shouldBe false
                }
                isInWatchlistFlow.value = true

                Then("the screenplay with extras is emitted") {
                    with(awaitItem().getOrNull().shouldNotBeNull()) {
                        screenplay shouldBe ScreenplaySample.Inception
                        isInWatchlist shouldBe true
                    }
                }
            }
        }
    }
})

private class RealGetScreenplayWithExtrasTestScenario(
    val sut: RealGetScreenplayWithExtras
)

private fun TestScenario(
    screenplay: Screenplay,
    credits: ScreenplayCredits,
    genres: ScreenplayGenres,
    keywords: ScreenplayKeywords,
    media: ScreenplayMedia,
    personalRating: Option<Rating>,
    isInWatchlist: Boolean = false,
    isInWatchlistFlow: Flow<Boolean> = flowOf(isInWatchlist)
) = RealGetScreenplayWithExtrasTestScenario(
    sut = RealGetScreenplayWithExtras(
        getExtra = FakeGetExtra(
            credits = credits,
            genres = genres,
            keywords = keywords,
            media = media,
            personalRating = PersonalRating(personalRating),
            isInWatchlistFlow = isInWatchlistFlow.map(::IsInWatchlist)
        ),
        screenplayStore = FakeScreenplayStore(screenplay)
    )
)