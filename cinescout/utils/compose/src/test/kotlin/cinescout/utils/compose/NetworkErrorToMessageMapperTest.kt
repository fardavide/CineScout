package cinescout.utils.compose

import cinescout.error.NetworkError
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.utils.android.NetworkErrorToMessageMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class NetworkErrorToMessageMapperTest : BehaviorSpec({

    val mapper = NetworkErrorToMessageMapper()
    
    Given("mapper") {

        When("error is BadRequest") {
            val input = NetworkError.BadRequest

            Then("should return unknown error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_unknown)
            }
        }
        
        When("error is Forbidden") {
            val input = NetworkError.Forbidden

            Then("should return forbidden error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_forbidden)
            }
        }
        
        When("error is Internal") {
            val input = NetworkError.Internal

            Then("should return internal error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_internal)
            }
        }
        
        When("error is NoNetwork") {
            val input = NetworkError.NoNetwork

            Then("should return no network error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_no_network)
            }
        }
        
        When("error is NotFound") {
            val input = NetworkError.NotFound

            Then("should return not found error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_not_found)
            }
        }
        
        When("error is Unauthorized") {
            val input = NetworkError.Unauthorized

            Then("should return unauthorized error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_unauthorized)
            }
        }
        
        When("error is Unknown") {
            val input = NetworkError.Unknown

            Then("should return unknown error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_unknown)
            }
        }
        
        When("error is Unreachable") {
            val input = NetworkError.Unreachable

            Then("should return unreachable error message") {
                val result = mapper.toMessage(input)
                result shouldBe TextRes(string.network_error_unreachable)
            }
        }
    }
})
