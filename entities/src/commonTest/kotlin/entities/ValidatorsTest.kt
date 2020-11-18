@file:Suppress("DataClassPrivateConstructor")

package entities

import assert4k.*
import entities.Validable.Companion.validate
import entities.model.InvalidEmailError
import kotlin.test.*

/**
 * Test suite for built-in [Validator]s
 * @author Davide Farella
 */
internal class ValidatorsTest {

    private object PositiveNumberError : ValidationError

    private data class PositiveNumber private constructor(val number: Int):
        Validable<PositiveNumberError> by Validator({
            conditionally(number > 0, { PositiveNumberError }, { PositiveNumber(number) })
        }) {

        companion object {
            operator fun invoke(number: Int) = PositiveNumber(number).validate()
        }
    }

    @Test
    fun `custom Validator works correctly`() {

        // isValid
        assert that PositiveNumber(10).isRight()
        assert that ! PositiveNumber(-10).isRight()

        // validate()
        assert that PositiveNumber(10) * {
            it `is` type<Either<PositiveNumberError, PositiveNumber>>()
            +isRight() equals true
            +rightOrNull() equals PositiveNumber(10).requireValid()
        }
        assert that PositiveNumber(-10) * {
            it `is` type<Either<PositiveNumberError, PositiveNumber>>()
            +isLeft() equals true
            +leftOrNull() equals PositiveNumberError
        }

        // validOrNull
        assert that PositiveNumber(10).rightOrNull() equals PositiveNumber(10).requireValid()
        assert that PositiveNumber(-10).rightOrNull() `is` Null
    }

    class SomeText private constructor (string: String):
        Validable<BlankStringError> by NotBlankStringValidator(::SomeText, string) {

        companion object {

            operator fun invoke(string: String) = SomeText(string).validate()
        }
    }

    @Test
    fun `NotBlackStringValidator works correctly`() {

        SomeText("hello")
        assert that fails<ValidationException> { SomeText("").requireValid() }
        assert that fails<ValidationException> { SomeText(" ").requireValid() }
    }

    // Regex is representative only for this test case and not intended to properly validate an email address
    class EmailRegexValidable private constructor (string: String): Validable<RegexMismatchError> by RegexValidator(
        ::EmailRegexValidable,
        string,
        "\\w+@[a-z]+\\.[a-z]+",
        InvalidEmailError.WrongFormat
    ) {

        companion object {

            operator fun invoke(string: String) = EmailRegexValidable(string).validate()
        }
    }

    @Test
    fun `RegexValidator works correctly`() {
        assert that EmailRegexValidable("somebody@protonmail.com").isRight()
        assert that ! EmailRegexValidable("somebody@123.456").isRight()
    }

}
