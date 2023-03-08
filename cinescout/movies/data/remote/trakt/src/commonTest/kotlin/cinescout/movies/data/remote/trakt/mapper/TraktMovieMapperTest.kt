package cinescout.movies.data.remote.trakt.mapper

import cinescout.movies.data.remote.sample.TraktMovieRatingSample
import cinescout.movies.data.remote.trakt.sample.GetRatingsSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TraktMovieMapperTest : BehaviorSpec({

    val mapper = TraktMovieMapper()

    Given("a movie rating") {
        val input = GetRatingsSample.Inception

        When("mapping to movie rating") {
            val result = mapper.toMovieRating(input)

            Then("it maps correctly") {
                result shouldBe TraktMovieRatingSample.Inception
            }
        }
    }
})
