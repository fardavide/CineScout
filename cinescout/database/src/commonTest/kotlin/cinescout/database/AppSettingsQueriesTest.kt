package cinescout.database

import cinescout.database.model.DatabaseAppSettings
import cinescout.database.testdata.DatabaseAppSettingsTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppSettingsQueriesTest : DatabaseTest() {

    private val queries = database.appSettingsQueries

    @Test
    fun default() {
        // given
        val expected = DatabaseAppSettingsTestData.Default

        // when
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected.id, result)
    }

    @Test
    fun insertAndFind() {
        // given
        val expected = DatabaseAppSettings(
            id = 1
        )

        // when
        queries.insert()
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected.id, result)
    }
}
