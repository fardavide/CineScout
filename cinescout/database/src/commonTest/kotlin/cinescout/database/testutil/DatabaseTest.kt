package cinescout.database.testutil

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import cinescout.database.Database
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

abstract class DatabaseTest : AnnotationSpec() {

    private lateinit var driver: JdbcSqliteDriver
    protected lateinit var database: Database

    override suspend fun beforeEach(testCase: TestCase) {
        driver = TestDatabase.createDriver()
        database = TestDatabase.createDatabase(driver)
        Database.Schema.create(driver)
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        driver.close()
    }
}
