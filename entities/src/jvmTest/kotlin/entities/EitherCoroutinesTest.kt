@file:Suppress("DataClassPrivateConstructor")

package entities

import assert4k.*
import entities.Validable.Companion.validate
import kotlinx.coroutines.flow.first
import util.test.CoroutinesTest
import kotlin.test.*

internal class EitherCoroutinesTest : CoroutinesTest {

    private interface PositiveNumberError : ValidationError
    private object NegativeNumber : PositiveNumberError
    private object SecondError : PositiveNumberError
    private object ThirdError : PositiveNumberError

    private data class PositiveNumber private constructor(val number: Int):
        Validable<PositiveNumberError> by Validator({
            conditionally(number > 0, { NegativeNumber }, { PositiveNumber(number) })
        }) {

        companion object {
            operator fun invoke(number: Int) = PositiveNumber(number).validate()
        }

        operator fun times(other: Int) = PositiveNumber(number * other)
    }

    private fun second(first: PositiveNumber, multiplier: Int): Either<SecondError, PositiveNumber> =
        if (multiplier >= 0) (first * multiplier).right()
        else SecondError.left()

    private fun third(second: PositiveNumber, multiplier: Int): Either<ThirdError, PositiveNumber> =
        if (multiplier >= 0) (second * multiplier).right()
        else ThirdError.left()

    // region fixFlow
    @Test
    fun `fixFlow return the correct Right`() = coroutinesTest {

        val result = Either.fixFlow<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, 2)

            emit(third(two, 2))
        }

        assert that result.first().rightOrThrow().number equals 12
    }

    @Test
    fun `fixFlow return the correct Error if first fails`() = coroutinesTest {

        val result = Either.fixFlow<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(-3)
            val (two) = second(one, -2)
            emit(third(two, 2))
        }

        assert that result.first().leftOrNull() equals NegativeNumber
    }

    @Test
    fun `fixFlow return the correct Error if second fails`() = coroutinesTest {

        val result = Either.fixFlow<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, -2)
            emit(third(two, 2))
        }

        assert that result.first().leftOrNull() equals SecondError
    }

    @Test
    fun `fixFlow return the correct Error if third fails`() = coroutinesTest {

        val result = Either.fixFlow<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, 2)
            emit(third(two, -2))
        }

        assert that result.first().leftOrNull() equals ThirdError
    }

    @Test
    fun `fixFlow return explicit error`() = coroutinesTest {

        val result = Either.fixFlow<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, -2) or NegativeNumber
            emit(third(two, 2))
        }

        assert that result.first().leftOrNull() equals NegativeNumber
    }
    // endregion
}
