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
                "No network",
                NetworkError.NoNetwork,
                TextRes(string.error_no_network)
            )

        ).map { arrayOf(it.testName, it.input, it.expected) }
    }
}
