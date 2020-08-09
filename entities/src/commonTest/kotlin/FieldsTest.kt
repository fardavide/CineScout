import assert4k.*
import kotlin.test.Test

class FieldsTest {

    @Test
    fun `FiveYearRange for year`() {
        assert that FiveYearRange(forYear = 2000u) equals FiveYearRange(2005u)
        assert that FiveYearRange(forYear = 2001u) equals FiveYearRange(2005u)
        assert that FiveYearRange(forYear = 2020u) equals FiveYearRange(2020u)
        assert that FiveYearRange(forYear = 2028u) equals FiveYearRange(2030u)
    }

}
