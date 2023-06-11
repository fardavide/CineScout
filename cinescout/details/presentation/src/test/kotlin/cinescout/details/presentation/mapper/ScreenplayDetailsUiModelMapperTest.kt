package cinescout.details.presentation.mapper

import arrow.core.toOption
import cinescout.details.domain.model.ScreenplayWithExtra
import cinescout.details.presentation.state.DetailsSeasonsState
import cinescout.history.domain.sample.HistorySample
import cinescout.media.domain.sample.ScreenplayMediaSample
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.sample.ScreenplayPersonalRatingSample
import cinescout.resources.R.plurals
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.model.movie
import cinescout.screenplay.domain.model.rating
import cinescout.screenplay.domain.model.voteCount
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.watchlist.domain.sample.ScreenplayWatchlistSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ScreenplayDetailsUiModelMapperTest : BehaviorSpec({
    val mapper = ScreenplayDetailsUiModelMapper()

    Given("a screenplay with 200 votes") {
        val screenplay = Screenplay.movie.rating.voteCount.set(ScreenplaySample.Inception, 200)
        val screenplayWithExtra = buildWithExtra(screenplay = screenplay)

        When("when mapped") {
            val uiModel = mapper.toUiModel(screenplayWithExtra, DetailsSeasonsState.NoSeasons)

            Then("the rating count should be 200 votes") {
                uiModel.ratingCount shouldBe TextRes.plural(plurals.details_votes, quantity = 200, 200)
            }
        }
    }

    Given("a screenplay with 2_000 votes") {
        val screenplay = Screenplay.movie.rating.voteCount.set(ScreenplaySample.Inception, 2_000)
        val screenplayWithExtra = buildWithExtra(screenplay = screenplay)

        When("when mapped") {
            val uiModel = mapper.toUiModel(screenplayWithExtra, DetailsSeasonsState.NoSeasons)

            Then("the rating count should be 2.0 k votes") {
                uiModel.ratingCount shouldBe TextRes(string.details_votes_k, "2.0")
            }
        }
    }

    Given("a screenplay with 54_321 votes") {
        val screenplay = Screenplay.movie.rating.voteCount.set(ScreenplaySample.Inception, 54_321)
        val screenplayWithExtra = buildWithExtra(screenplay = screenplay)

        When("when mapped") {
            val uiModel = mapper.toUiModel(screenplayWithExtra, DetailsSeasonsState.NoSeasons)

            Then("the rating count should be 54.3 k votes") {
                uiModel.ratingCount shouldBe TextRes(string.details_votes_k, "54.3")
            }
        }
    }
})

private fun buildWithExtra(screenplay: Screenplay) = ScreenplayWithExtra(
    screenplay = screenplay,
    credits = ScreenplayCreditsSample.Inception,
    genres = ScreenplayGenresSample.Inception,
    history = HistorySample.Inception,
    isInWatchlist = ScreenplayWatchlistSample.Inception,
    keywords = ScreenplayKeywordsSample.Inception,
    media = ScreenplayMediaSample.Inception,
    personalRating = ScreenplayPersonalRatingSample.Inception.getOrThrow().toOption()
)
