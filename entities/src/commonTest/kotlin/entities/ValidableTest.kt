@file:Suppress("DataClassPrivateConstructor")

package entities

import assert4k.*
import entities.model.EmailAddress
import kotlin.test.*

internal class ValidableTest {

    @Test
    fun `isValid return proper Boolean`() {
        assert that EmailAddress("somebody@email.com").isRight()
        assert that ! EmailAddress("invalid").isRight()
    }

    @Test
    fun `validate returns proper Result`() {
        assert that EmailAddress("somebody@email.com").isRight()
        assert that EmailAddress("invalid").isLeft()
    }

    @Test
    fun `requireValid throws when Validable is not valid`() {

        EmailAddress("somebody@email.com").requireValid()

        assert that fails<ValidationException> {
            EmailAddress("invalid").requireValid()
        } with "'requireValid' on Validable thrown with reason: WrongFormat"
    }

    @Test
    fun `validOrNull return Validable if success`() {
        assert that EmailAddress("hello@mail.com").rightOrNull() equals
            EmailAddress("hello@mail.com").requireValid()
    }

    @Test
    fun `validOrNull return null if failure`() {
        assert that EmailAddress("hello@.com").rightOrNull() `is` `null`
    }
}
