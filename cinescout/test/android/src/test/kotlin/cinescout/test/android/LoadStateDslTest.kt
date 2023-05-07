package cinescout.test.android

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class LoadStateDslTest : BehaviorSpec({

    Given("a CombinedLoadState") {

        When("create with lambda") {
            val combinedLoadState = buildCombinedLoadStates {
                refresh { loading() }
                prepend { incomplete() }
                append { error(EqualException("message")) }
                source {
                    refresh { complete() }
                    prepend { error(EqualException("another message")) }
                    append { loading() }
                }
                mediator {
                    refresh { error(EqualException("yet another message")) }
                    prepend { complete() }
                    append { incomplete() }
                }
            }

            Then("result is correct") {
                combinedLoadState shouldBe CombinedLoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = false),
                    append = LoadState.Error(EqualException("message")),
                    source = LoadStates(
                        refresh = LoadState.NotLoading(endOfPaginationReached = true),
                        prepend = LoadState.Error(EqualException("another message")),
                        append = LoadState.Loading
                    ),
                    mediator = LoadStates(
                        refresh = LoadState.Error(EqualException("yet another message")),
                        prepend = LoadState.NotLoading(endOfPaginationReached = true),
                        append = LoadState.NotLoading(endOfPaginationReached = false)
                    )
                )
            }
        }

        When("create with inline source") {
            val combinedLoadState by CombinedLoadStates.source.refresh.complete()

            Then("result is correct") {
                combinedLoadState shouldBe buildCombinedLoadStates {
                    source { refresh { complete() } }
                }
            }
        }

        When("crate with inline refresh") {
            val combinedLoadState by CombinedLoadStates.refresh.complete()

            Then("result is correct") {
                combinedLoadState shouldBe buildCombinedLoadStates {
                    refresh { complete() }
                }
            }
        }
    }
})

private data class EqualException(override val message: String) : Exception(message)
