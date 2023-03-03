package cinescout.test.database

import cinescout.database.Database
import cinescout.database.testutil.TestDatabase
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec

class TestDatabaseExtension : BeforeSpecListener, AfterSpecListener {

    private val driver = TestDatabase.createDriver()
    val database = TestDatabase.createDatabase(driver)

    override suspend fun beforeSpec(spec: Spec) {
        Database.Schema.create(driver)
    }

    override suspend fun afterSpec(spec: Spec) {
        driver.close()
    }
}
