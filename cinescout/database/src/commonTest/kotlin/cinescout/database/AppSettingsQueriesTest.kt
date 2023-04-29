package cinescout.database

import cinescout.database.sample.DatabaseAppSettingsSample
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class AppSettingsQueriesTest : DatabaseTest() {

    private val queries get() = database.appSettingsQueries

    @Test
    fun insertAndFind() {
        // given
        val appSettings = DatabaseAppSettingsSample.Default

        // when
        queries.insert(appSettings)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(appSettings, result)
    }
}
