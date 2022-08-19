package cinescout.store

import cinescout.database.StoreFetchData
import cinescout.database.StoreFetchDataQueries
import com.soywiz.klock.DateTime
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import store.FetchData
import store.StoreKey
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreFetchDataRepositoryTest {

    private val dispatcher = StandardTestDispatcher()
    private val queries: StoreFetchDataQueries = mockk()
    private val repository = StoreFetchDataRepository(ioDispatcher = dispatcher, queries = queries)

    @Test
    fun `returns null if not fetch data available for a given key`() = runTest(dispatcher) {
        // given
        val key = StoreKey("123")
        val expected = null
        every { queries.find(key.value).executeAsOneOrNull() } returns null

        // when
        val result = repository.getFetchData(key)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns correct fetch data if available for a given key`() = runTest(dispatcher) {
        // given
        val key = StoreKey("123")
        val dateTime = DateTime.now()
        val expected = FetchData(dateTime = dateTime)
        every { queries.find(key.value).executeAsOneOrNull() } returns StoreFetchData(key.value, dateTime)

        // when
        val result = repository.getFetchData(key)

        // then
        assertEquals(expected, result)
    }
}
