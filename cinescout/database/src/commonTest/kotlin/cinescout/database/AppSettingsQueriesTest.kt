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
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFind() {
        // given
        val expected = DatabaseAppSettings(
            id = 1,
            hasShownForYouHint = true
        )

        // when
        queries.insert(hasShownForYouHint = true)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun setHasShownForYouHint() {
        // given
        val expected = DatabaseAppSettings(
            id = 1,
            hasShownForYouHint = true
        )

        // when
        queries.setHasShownForYouHint(hasShownForYouHint = true)
        val result = queries.find().executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
