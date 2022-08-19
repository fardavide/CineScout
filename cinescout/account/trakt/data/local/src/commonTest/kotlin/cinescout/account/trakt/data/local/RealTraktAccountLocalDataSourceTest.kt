package cinescout.account.trakt.data.local

import app.cash.turbine.test
import cinescout.account.trakt.data.local.mapper.TraktAccountMapper
import cinescout.account.trakt.domain.testData.TraktAccountTestData
import cinescout.database.Database
import cinescout.database.testutil.TestDatabase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTraktAccountLocalDataSourceTest {

    private val driver by lazy { TestDatabase.createDriver() }
    private val database by lazy { TestDatabase.createDatabase(driver) }
    private val dispatcher = StandardTestDispatcher()
    private val source by lazy {
        RealTraktAccountLocalDataSource(
            accountMapper = TraktAccountMapper(),
            accountQueries = database.traktAccountQueries,
            dispatcher = dispatcher
        )
    }

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `insert and find account`() = runTest(dispatcher) {
        // given
        val account = TraktAccountTestData.Account

        // when
        source.insert(account)
        source.findAccount().test {

            // then
            assertEquals(account, awaitItem())
        }
    }
}
