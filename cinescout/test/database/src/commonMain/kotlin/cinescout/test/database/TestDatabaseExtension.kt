package cinescout.test.database

import cinescout.database.Database
import cinescout.database.testutil.TestDatabase
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec

class TestDatabaseExtension : BeforeSpecListener, AfterSpecListener {

    private var driver = TestDatabase.createDriver()
    var database = TestDatabase.createDatabase(driver)
        private set

    fun clear() {
        driver.close()
        driver = TestDatabase.createDriver()
        database = TestDatabase.createDatabase(driver)
        Database.Schema.create(driver)
    }

    override suspend fun beforeSpec(spec: Spec) {
        Database.Schema.create(driver)
    }

    override suspend fun afterSpec(spec: Spec) {
        driver.close()
    }
}

fun Spec.requireTestDatabaseExtension(): TestDatabaseExtension =
    registeredExtensions().filterIsInstance<TestDatabaseExtension>().first()
