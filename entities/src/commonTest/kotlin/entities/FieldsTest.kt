package entities

import assert4k.*
import entities.field.EmailAddress
import entities.field.FiveYearRange
import entities.field.ImageUrl
import entities.field.ImageUrl.Size.Original
import entities.field.ImageUrl.Size.W500
import kotlin.test.*

class FieldsTest {

    @Test
    fun `verify EmailAddress happy paths`() {
        listOf(
            "mail@4face.studio",
            "HellO.be_autiful@world.com",
            "davide@proton.me",
            "hello@email.it"
        ).map { EmailAddress(it).requireValid() }
    }

    @Test
    fun `verify EmailAddress failing paths`() {

        assert that fails<ValidationException> {
            EmailAddress("@hello.com").requireValid()
        }
        assert that fails<ValidationException> {
            EmailAddress("hello.com").requireValid()
        }
        assert that fails<ValidationException> {
            EmailAddress("hello@com").requireValid()
        }
        assert that fails<ValidationException> {
            EmailAddress("hello@.com").requireValid()
        }
        assert that fails<ValidationException> {
            EmailAddress("hello@-.com").requireValid()
        }
    }

    @Test
    fun `FiveYearRange for year`() {
        assert that FiveYearRange(forYear = 2000u) equals FiveYearRange(2005u)
        assert that FiveYearRange(forYear = 2001u) equals FiveYearRange(2005u)
        assert that FiveYearRange(forYear = 2020u) equals FiveYearRange(2020u)
        assert that FiveYearRange(forYear = 2028u) equals FiveYearRange(2030u)
    }

    @Test
    fun `Poster build proper url with or without slash`() {
        val poster1 = ImageUrl("base", "path")
        assert that poster1.get(Original) equals "base/original/path"

        val poster2 = ImageUrl("base/", "/path")
        assert that poster2.get(W500) equals "base/w500/path"
    }

}
