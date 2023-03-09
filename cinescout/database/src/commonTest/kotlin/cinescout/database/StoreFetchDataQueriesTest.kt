package cinescout.database

import cinescout.database.testutil.DatabaseTest
import com.soywiz.klock.DateTime
import org.junit.jupiter.api.Assertions.assertEquals

class StoreFetchDataQueriesTest : DatabaseTest() {

    private val queries get() = database.storeFetchDataQueries

    @Test
    fun insertAndFindStoreFetchData() {
        // given
        val key = "getTmdbAccount(123)"
        val dateTime = DateTime.now()
        val expected = StoreFetchData(key, dateTime)

        // when
        queries.insert(key = key, dateTime = dateTime)
        val result = queries.find(key).executeAsOneOrNull()

        // then
        assertEquals(expected, result)
    }
}
