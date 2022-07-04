package cinescout.database.testutil

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import cinescout.database.Database
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class DatabaseTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    protected val database = Database(
        driver = driver,
        movieAdapter = MovieAdapter
    )

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }
}
