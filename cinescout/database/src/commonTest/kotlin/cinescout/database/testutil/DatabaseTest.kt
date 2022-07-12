package cinescout.database.testutil

import cinescout.database.Database
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class DatabaseTest {

    private val driver = TestDatabase.createDriver()
    protected val database = TestDatabase.createDatabase(driver)

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }
}
