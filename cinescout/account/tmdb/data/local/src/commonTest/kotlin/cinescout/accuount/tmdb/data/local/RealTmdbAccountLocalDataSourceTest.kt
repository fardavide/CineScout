package cinescout.accuount.tmdb.data.local

import app.cash.turbine.test
import arrow.core.right
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import cinescout.accuount.tmdb.data.local.mapper.TmdbAccountMapper
import cinescout.database.Database
import cinescout.database.testutil.TestDatabase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealTmdbAccountLocalDataSourceTest {

    private val driver by lazy { TestDatabase.createDriver() }
    private val database by lazy { TestDatabase.createDatabase(driver) }
    private val dispatcher = StandardTestDispatcher()
    private val source by lazy {
        RealTmdbAccountLocalDataSource(
            accountMapper = TmdbAccountMapper(),
            accountQueries = database.tmdbAccountQueries,
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
        val account = TmdbAccountTestData.Account
        val expected = account.right()

        // when
        source.insert(account)
        source.findAccount().test {

            // then
            assertEquals(expected, awaitItem())
        }
    }
}
