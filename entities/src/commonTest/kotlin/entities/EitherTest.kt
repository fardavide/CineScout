@file:Suppress("DataClassPrivateConstructor")

package entities

import assert4k.*
import entities.Validable.Companion.validate
import kotlin.test.*

internal class EitherTest {

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

    @Test
    fun `fix return the correct Right`() {

        val result = Either.fix<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, 2)
            third(two, 2)
        }

        assert that result.rightOrThrow().number equals 12
    }

    @Test
    fun `fix return the correct Error if first fails`() {

        val result = Either.fix<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(-3)
            val (two) = second(one, -2)
            third(two, 2)
        }

        assert that result.leftOrNull() equals NegativeNumber
    }

    @Test
    fun `fix return the correct Error if second fails`() {

        val result = Either.fix<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, -2)
            third(two, 2)
        }

        assert that result.leftOrNull() equals SecondError
    }

    @Test
    fun `fix return the correct Error if third fails`() {

        val result = Either.fix<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, 2)
            third(two, -2)
        }

        assert that result.leftOrNull() equals ThirdError
    }

    @Test
    fun `fix return explicit error`() {

        val result = Either.fix<Error, PositiveNumber, Error> {
            val (one) = PositiveNumber(3)
            val (two) = second(one, -2) or NegativeNumber
            third(two, 2)
        }

        assert that result.leftOrNull() equals NegativeNumber
    }
}
