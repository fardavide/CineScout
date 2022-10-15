plugins {
    id("cinescout.kotlin")
    id("cinescout.android")
}

moduleDependencies {
    account {
        tmdb.data.remote()
        trakt.data.remote()
    }
    auth {
        tmdb.data.remote()
        trakt.data.remote()
    }
    database()
    movies {
        data {
            this()
            remote {
                tmdb()
                trakt()
            }
        }
        domain()
    }
    network {
        this()
        tmdb()
        trakt()
    }
    settings.domain()
    store()
    suggestions.domain()
    tvShows {
        data {
            this()
            remote {
                tmdb()
                trakt()
            }
        }
        domain()
    }
}

dependencies {
    commonMainCompileOnly(libs.bundles.base)
    commonMainCompileOnly(libs.bundles.test.kotlin)

    commonMainCompileOnly(libs.koin.core)
    commonMainCompileOnly(libs.koin.test)
    commonMainCompileOnly(libs.koin.test.junit4)
    commonMainCompileOnly(libs.ktor.client.mock)
    commonMainCompileOnly(libs.sqlDelight.sqlite)
}

kotlin {
    jvm()
    android()

    sourceSets.named("jvmMain") {
        dependencies {
            compileOnly(libs.sqlDelight.sqlite)
        }
    }
    sourceSets.named("androidMain") {
        dependencies {
            compileOnly(libs.sqlDelight.android)
        }
    }
}
