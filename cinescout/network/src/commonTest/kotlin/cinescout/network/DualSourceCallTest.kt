package cinescout.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class DualSourceCallTest(
    private val testName: String,
    private val input: Input,
    private val expected: Either<NetworkError, Unit>
) {

    @Test
    fun test() = runTest {
        println(testName)
        // given
        val call = DualSourceCall(
            isFirstSourceLinked = { input.isFirstSourceLinked },
            isSecondSourceLinked = { input.isSecondSourceLinked }
        )

        // when
        val result = call(
            firstSourceCall = { input.firstSourceResult },
            secondSourceCall = { input.secondSourceResult }
        )

        // then
        assertEquals(expected, result)
    }

    data class Input(
        val isFirstSourceLinked: Boolean,
        val isSecondSourceLinked: Boolean,
        val firstSourceResult: Either<NetworkError, Unit>,
        val secondSourceResult: Either<NetworkError, Unit>
    )

    data class Params(
        val testName: String,
        val input: Input,
        val expected: Either<NetworkError, Unit>
    )

    companion object {

        @JvmStatic
        @Parameters(name = "{0}")
        fun data() = listOf(

            Params(
                testName = "Succeed if both source is linked and both succeed",
                input = Input(
                    isFirstSourceLinked = true,
                    isSecondSourceLinked = true,
                    firstSourceResult = Unit.right(),
                    secondSourceResult = Unit.right()
                ),
                expected = Unit.right()
            ),

            Params(
                testName = "Succeed if first source is linked and both succeed",
                input = Input(
                    isFirstSourceLinked = true,
                    isSecondSourceLinked = false,
                    firstSourceResult = Unit.right(),
                    secondSourceResult = Unit.right()
                ),
                expected = Unit.right()
            ),

            Params(
                testName = "Succeed if second source is linked and both succeed",
                input = Input(
                    isFirstSourceLinked = false,
                    isSecondSourceLinked = true,
                    firstSourceResult = Unit.right(),
                    secondSourceResult = Unit.right()
                ),
                expected = Unit.right()
            ),

            Params(
                testName = "Succeed if only first source is linked and second fails",
                input = Input(
                    isFirstSourceLinked = true,
                    isSecondSourceLinked = false,
                    firstSourceResult = Unit.right(),
                    secondSourceResult = NetworkError.Internal.left()
                ),
                expected = Unit.right()
            ),

            Params(
                testName = "Fails with unauthorized if no source is linked and both succeed",
                input = Input(
                    isFirstSourceLinked = false,
                    isSecondSourceLinked = false,
                    firstSourceResult = Unit.right(),
                    secondSourceResult = Unit.right()
                ),
                expected = NetworkError.Unauthorized.left()
            ),

            Params(
                testName = "Fails with unauthorized if no source is linked and both fails",
                input = Input(
                    isFirstSourceLinked = false,
                    isSecondSourceLinked = false,
                    firstSourceResult = NetworkError.Internal.left(),
                    secondSourceResult = NetworkError.Internal.left()
                ),
                expected = NetworkError.Unauthorized.left()
            ),

            Params(
                testName = "Fails with first error if both source are linked and first fails",
                input = Input(
                    isFirstSourceLinked = true,
                    isSecondSourceLinked = true,
                    firstSourceResult = NetworkError.Internal.left(),
                    secondSourceResult = Unit.right()
                ),
                expected = NetworkError.Internal.left()
            ),

            Params(
                testName = "Fails with second error if both source are linked and second fails",
                input = Input(
                    isFirstSourceLinked = true,
                    isSecondSourceLinked = true,
                    firstSourceResult = Unit.right(),
                    secondSourceResult = NetworkError.Internal.left()
                ),
                expected = NetworkError.Internal.left()
            ),

            Params(
                testName = "Fails with first error if both source are linked and both fail",
                input = Input(
                    isFirstSourceLinked = true,
                    isSecondSourceLinked = true,
                    firstSourceResult = NetworkError.Internal.left(),
                    secondSourceResult = NetworkError.Forbidden.left()
                ),
                expected = NetworkError.Internal.left()
            )

        ).map { arrayOf(it.testName, it.input, it.expected) }
    }
}
