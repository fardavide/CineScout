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
import cinescout.media.domain.usecase.FakeGetScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.people.domain.usecase.FakeGetScreenplayCredits
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.rating.domain.usecase.FakeGetPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.store.FakeScreenplayStore
import cinescout.screenplay.domain.usecase.FakeGetScreenplayGenres
import cinescout.screenplay.domain.usecase.FakeGetScreenplayKeywords
import cinescout.watchlist.domain.sample.ScreenplayWatchlistSample
import cinescout.watchlist.domain.usecase.FakeGetIsScreenplayInWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

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

            Then("the screenplay with extras is returned") {
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
    isInWatchlist: Boolean
) = RealGetScreenplayWithExtrasTestScenario(
    sut = RealGetScreenplayWithExtras(
        getCredits = FakeGetScreenplayCredits(credits),
        getGenres = FakeGetScreenplayGenres(genres),
        getIsInWatchlist = FakeGetIsScreenplayInWatchlist(isInWatchlist),
        getKeywords = FakeGetScreenplayKeywords(keywords),
        getMedia = FakeGetScreenplayMedia(media),
        getPersonalRating = FakeGetPersonalRating(personalRating),
        screenplayStore = FakeScreenplayStore(screenplay)
    )
)
