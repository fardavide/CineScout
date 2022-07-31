package cinescout.design

import cinescout.error.NetworkError
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import studio.forface.cinescout.design.R.string
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class NetworkErrorToMessageMapperTest(
    private val testName: String,
    private val input: NetworkError,
    private val expected: TextRes
) {

    private val mapper = NetworkErrorToMessageMapper()

    @Test
    fun test() {
        println("Running: $testName")
        val result = mapper.toMessage(input)
        assertEquals(expected, result)
    }

    data class Params(
        val testName: String,
        val input: NetworkError,
        val expected: TextRes
    )

    companion object {

        @JvmStatic
        @Parameters(name = "{0}")
        fun data() = listOf(

            Params(
                "Forbidden",
                NetworkError.Forbidden,
                TextRes(string.network_error_forbidden)
            ),

            Params(
                "No network",
                NetworkError.NoNetwork,
                TextRes(string.network_error_no_network)
            ),

            Params(
                "Not found",
                NetworkError.NotFound,
                TextRes(string.network_error_not_found)
            ),

            Params(
                "Internal",
                NetworkError.Internal,
                TextRes(string.network_error_internal)
            ),

            Params(
                "Unauthorized",
                NetworkError.Unauthorized,
                TextRes(string.network_error_unauthorized)
            ),

            Params(
                "Unreachable",
                NetworkError.Unreachable,
                TextRes(string.network_error_unreachable)
            )

        ).map { arrayOf(it.testName, it.input, it.expected) }
    }
}
