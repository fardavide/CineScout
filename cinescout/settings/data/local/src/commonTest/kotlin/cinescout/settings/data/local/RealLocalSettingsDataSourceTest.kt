package cinescout.settings.data.local

import cinescout.database.Database
import cinescout.database.testutil.TestDatabase
import cinescout.settings.data.local.datasource.RealLocalSettingsDataSource
import cinescout.settings.data.local.mapper.DatabaseAppSettingsMapper
import cinescout.test.kotlin.TestTimeout
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

internal class RealLocalSettingsDataSourceTest : AnnotationSpec() {

    private val driver by lazy { TestDatabase.createDriver() }
    private val database by lazy { TestDatabase.createDatabase(driver) }
    private val scheduler = TestCoroutineScheduler()
    private val scope = TestScope(scheduler)
    private val appSettingsQueries = spyk(database.appSettingsQueries)
    private val ioDispatcher = StandardTestDispatcher(scheduler)

    private val dataSource = RealLocalSettingsDataSource(
        appScope = scope,
        appSettingsQueries = appSettingsQueries,
        ioDispatcher = ioDispatcher,
        mapper = DatabaseAppSettingsMapper(),
        writeDispatcher = ioDispatcher
    )

    @Before
    fun setup() {
        Database.Schema.create(driver)
    }

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `emits app settings to each observer`() = runTest(
        scheduler,
        timeout = TestTimeout
    ) {
        // when
        dataSource.appSettings.first() shouldNotBe null
        dataSource.appSettings.first() shouldNotBe null
    }
}
