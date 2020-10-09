@file:Suppress("DataClassPrivateConstructor")

package entities

import assert4k.*
import entities.Validable.Companion.validate
import kotlin.test.*

internal class ValidableTest {

    @Test
    fun `isValid return proper Boolean`() {
        assert that EmailTestValidable("somebody@email.com").isRight()
        assert that ! EmailTestValidable("invalid").isRight()
    }

    @Test
    fun `validate returns proper Result`() {
        assert that EmailTestValidable("somebody@email.com").isRight()
        assert that EmailTestValidable("invalid").isLeft()
    }

    @Test
    fun `requireValid throws when Validable is not valid`() {

        EmailTestValidable("somebody@email.com").requireValid()

        assert that fails<ValidationException> {
            EmailTestValidable("invalid").requireValid()
        } with "'requireValid' on Validable thrown with reason: RegexMismatchError"
    }

    @Test
    fun `validOrNull return Validable if success`() {
        assert that EmailTestValidable("hello@mail.com").rightOrNull() equals
            EmailTestValidable("hello@mail.com").requireValid()
    }

    @Test
    fun `validOrNull return null if failure`() {
        assert that EmailTestValidable("hello@.com").rightOrNull() `is` `null`
    }
}

private data class EmailTestValidable private constructor(val s: String):
    Validable<RegexMismatchError> by EmailTestValidator(::EmailTestValidable, s) {

    companion object {
        operator fun invoke(s: String) = EmailTestValidable(s).validate()
    }
}

// Regex is representative only for this test case and not intended to properly validate an email address
private fun <V : Validable<RegexMismatchError>> EmailTestValidator(v: (String) -> V, email: String) =
    RegexValidator(v, email, ".+@.+\\..+")
