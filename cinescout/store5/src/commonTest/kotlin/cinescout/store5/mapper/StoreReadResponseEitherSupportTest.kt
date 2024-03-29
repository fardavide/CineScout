package cinescout.store5.mapper

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.store5.FetchException
import cinescout.store5.SkippedFetch
import cinescout.store5.Store5ReadResponse
import cinescout.test.kotlin.FakeLogger
import co.touchlab.kermit.Severity
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin
import java.io.IOException

class StoreReadResponseEitherSupportTest : BehaviorSpec({

    Given("a StoreReadResponse.Data") {
        val value = "some data"

        When("origin is Fetcher") {
            val origin = StoreReadResponseOrigin.Fetcher()
            val response = StoreReadResponse.Data(value, origin)
            val result = response.toStore5ReadResponse()

            Then("result is Data") {
                result.shouldBeInstanceOf<Store5ReadResponse.Data<*>>()
            }
            result as Store5ReadResponse.Data<String>

            Then("value is right") {
                result.value shouldBe value.right()
            }

            Then("origin is fetcher") {
                result.origin shouldBe origin
            }
        }

        When("origin is Cache") {
            val origin = StoreReadResponseOrigin.Cache
            val response = StoreReadResponse.Data(value, origin)
            val result = response.toStore5ReadResponse()

            Then("result is Data") {
                result.shouldBeInstanceOf<Store5ReadResponse.Data<*>>()
            }
            result as Store5ReadResponse.Data<String>

            Then("value is right") {
                result.value shouldBe value.right()
            }

            Then("origin is cache") {
                result.origin shouldBe origin
            }
        }

        When("origin is SourceOfTruth") {
            val origin = StoreReadResponseOrigin.SourceOfTruth
            val response = StoreReadResponse.Data(value, origin)
            val result = response.toStore5ReadResponse()

            Then("result is Data") {
                result.shouldBeInstanceOf<Store5ReadResponse.Data<*>>()
            }
            result as Store5ReadResponse.Data<String>

            Then("value is right") {
                result.value shouldBe value.right()
            }

            Then("origin is source of truth") {
                result.origin shouldBe origin
            }
        }
    }

    Given("a StoreReadResponse.Error") {

        And("error is FetchException") {
            val networkError = NetworkError.BadRequest
            val exception = FetchException(networkError)

            When("origin is Fetcher") {
                val origin = StoreReadResponseOrigin.Fetcher()
                val response = StoreReadResponse.Error.Exception(exception, origin)
                val result = response.toStore5ReadResponse()

                Then("result is Data") {
                    result.shouldBeInstanceOf<Store5ReadResponse.Data<*>>()
                }
                result as Store5ReadResponse.Data<String>

                Then("value is left") {
                    result.value shouldBe networkError.left()
                }

                Then("origin is fetcher") {
                    result.origin shouldBe origin
                }
            }
        }

        And("error is SkippedFetch") {

            When("origin is Fetcher") {
                val response = StoreReadResponse.Error.Exception(SkippedFetch, StoreReadResponseOrigin.Fetcher())
                val result = response.toStore5ReadResponse()

                Then("result is Skipped") {
                    result.shouldBeInstanceOf<Store5ReadResponse.Skipped>()
                }
            }
        }

        When("error is another Exception") {
            val response = StoreReadResponse.Error.Exception(IOException("error"), StoreReadResponseOrigin.Fetcher())

            Then("throw exception") {
                shouldThrowWithMessage<RuntimeException>(
                    "java.io.IOException: error. Cause: null"
                ) { response.toStore5ReadResponse() }
            }
        }

        When("error is Message") {
            val response = StoreReadResponse.Error.Message("some error", StoreReadResponseOrigin.Fetcher())

            Then("throw exception") {
                shouldThrowWithMessage<IllegalStateException>(
                    "StoreReadResponse.Error.Message is not supported"
                ) { response.toStore5ReadResponse() }
            }
        }
    }

    Given("a StoreReadResponse.Loading") {
        When("origin is Fetcher") {
            val origin = StoreReadResponseOrigin.Fetcher()
            val response = StoreReadResponse.Loading(origin)
            val result = response.toStore5ReadResponse()

            Then("result is Loading") {
                result.shouldBeInstanceOf<Store5ReadResponse.Loading>()
            }
            result as Store5ReadResponse.Loading

            Then("origin is fetcher") {
                result.origin shouldBe origin
            }
        }

        When("origin is Cache") {
            val origin = StoreReadResponseOrigin.Cache
            val response = StoreReadResponse.Loading(origin)
            val result = response.toStore5ReadResponse()

            Then("result is Loading") {
                result.shouldBeInstanceOf<Store5ReadResponse.Loading>()
            }
            result as Store5ReadResponse.Loading

            Then("origin is cache") {
                result.origin shouldBe origin
            }
        }

        When("origin is SourceOfTruth") {
            val origin = StoreReadResponseOrigin.SourceOfTruth
            val response = StoreReadResponse.Loading(origin)
            val result = response.toStore5ReadResponse()

            Then("result is Loading") {
                result.shouldBeInstanceOf<Store5ReadResponse.Loading>()
            }
            result as Store5ReadResponse.Loading

            Then("origin is source of truth") {
                result.origin shouldBe origin
            }
        }
    }

    Given("a StoreReadResponse.NoNewData") {
        When("origin is Fetcher") {
            val origin = StoreReadResponseOrigin.Fetcher()
            val response = StoreReadResponse.NoNewData(origin)
            val result = response.toStore5ReadResponse()

            Then("result is Skipped") {
                result.shouldBeInstanceOf<Store5ReadResponse.Skipped>()
            }
            result as Store5ReadResponse.Skipped

            Then("origin is fetcher") {
                result.origin shouldBe origin
            }
        }
    }

    // https://github.com/fardavide/CineScout/issues/393
    Given("a StoreReadResponse.Data with null value") {
        val value: String? = null
        val logger = FakeLogger.init()

        When("mapping") {
            val origin = StoreReadResponseOrigin.Fetcher()

            @Suppress("UNCHECKED_CAST")
            val response = StoreReadResponse.Data(value, origin) as StoreReadResponse.Data<String>
            val result = response.toStore5ReadResponse()

            Then("result is Data") {
                result shouldBe Store5ReadResponse.Skipped
            }

            Then("error is logged") {
                with(logger.requireLastLogEvent()) {
                    message shouldBe "StoreReadResponse.Data.value is null: $response"
                    severity shouldBe Severity.Error
                }
            }
        }
    }
})
