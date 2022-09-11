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
    fun `returns null if fetch data not available for a given key`() = runTest(dispatcher) {
        // given
        val keyValue = StoreKey("123").value()
        val expected = null
        every { queries.find(keyValue.value).executeAsOneOrNull() } returns null

        // when
        val result = repository.getFetchData(keyValue)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns null if fetch data available for a key with another id`() = runTest(dispatcher) {
        // given
        val currentKeyValue = StoreKey("123").value()
        val anotherKeyValue = StoreKey("456").value()
        val expected = null
        every { queries.find(currentKeyValue.value).executeAsOneOrNull() } returns null
        every { queries.find(anotherKeyValue.value).executeAsOneOrNull() } returns
            StoreFetchData(anotherKeyValue.value, DateTime.now())

        // when
        val result = repository.getFetchData(currentKeyValue)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns null if fetch data available for a key with another item id`() = runTest(dispatcher) {
        // given
        val currentKeyValue = StoreKey("123", 1).value()
        val anotherKeyValue = StoreKey("123", 2).value()
        val expected = null
        every { queries.find(currentKeyValue.value).executeAsOneOrNull() } returns null
        every { queries.find(anotherKeyValue.value).executeAsOneOrNull() } returns
            StoreFetchData(anotherKeyValue.value, DateTime.now())

        // when
        val result = repository.getFetchData(currentKeyValue)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns correct fetch data if available for a given key`() = runTest(dispatcher) {
        // given
        val keyValue = StoreKey("123").value()
        val dateTime = DateTime.now()
        val expected = FetchData(dateTime = dateTime)
        every { queries.find(keyValue.value).executeAsOneOrNull() } returns StoreFetchData(keyValue.value, dateTime)

        // when
        val result = repository.getFetchData(keyValue)

        // then
        assertEquals(expected, result)
    }
}
