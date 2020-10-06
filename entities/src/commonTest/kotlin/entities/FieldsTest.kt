package entities

import assert4k.*
import entities.ImageUrl.Size.Original
import entities.ImageUrl.Size.W500
import kotlin.test.*

class FieldsTest {

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
