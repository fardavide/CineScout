plugins {
    id("cinescout.kotlin")
    id("cinescout.android")
    alias(libs.plugins.sqlDelight)
}

sqldelight {
    database("Database") {
        dialect(libs.sqlDelight.sqlite.dialect.get())
        packageName = "cinescout.database"
        schemaOutputDirectory = file("src/commonMain/sqldelight/cinescout/database/schemas")
        verifyMigrations = true
    }
}

moduleDependencies {
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.sqlDelight.coroutines)
    commonMainImplementation(libs.sqlDelight.sqlite)

    commonTestImplementation(libs.bundles.test.kotlin)

}

kotlin {
    jvm()
    android()

    sourceSets.named("jvmMain") {
        dependencies {
            implementation(libs.sqlDelight.sqlite)
        }
    }
    sourceSets.named("androidMain") {
        dependencies {
            implementation(libs.sqlDelight.android)
        }
    }
}
