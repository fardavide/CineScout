package cinescout.settings.data.local

import cinescout.database.Database
import cinescout.database.testutil.TestDatabase
import cinescout.test.kotlin.TestTimeoutMs
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

internal class RealLocalSettingsDataSourceTest {

    private val driver by lazy { TestDatabase.createDriver() }
    private val database by lazy { TestDatabase.createDatabase(driver) }
    private val scheduler = TestCoroutineScheduler()
    private val scope = TestScope(scheduler)
    private val appSettingsQueries = spyk(database.appSettingsQueries)
    private val ioDispatcher = StandardTestDispatcher(scheduler)

    private val dataSource = RealLocalSettingsDataSource(
        appScope = scope,
        appSettingsQueries = appSettingsQueries,
        ioDispatcher = ioDispatcher
    )

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `emits app settings to each observer`() = runTest(
        scheduler,
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // when
        assertNotNull(dataSource.appSettings.first())
        assertNotNull(dataSource.appSettings.first())
    }
}
