plugins {
    id("cinescout.kotlin")
    alias(libs.plugins.sqlDelight)
}

sqldelight {
    database("Database") {
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

    sourceSets.named("jvmMain") {
        dependencies {
            implementation(libs.sqlDelight.sqlite)
        }
    }
}
