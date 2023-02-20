package cinescout.accuount.tmdb.data.local

import app.cash.turbine.test
import cinescout.account.tmdb.data.local.RealTmdbAccountLocalDataSource
import cinescout.account.tmdb.data.local.mapper.TmdbAccountMapper
import cinescout.account.tmdb.domain.sample.TmdbAccountSample
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
            dispatcher = dispatcher,
            writeDispatcher = dispatcher
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
        val account = TmdbAccountSample.Account

        // when
        source.insert(account)
        source.findAccount().test {

            // then
            assertEquals(account, awaitItem())
        }
    }
}
