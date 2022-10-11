plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    account {
        domain()
        tmdb {
            data {
                this()
                local()
                remote()
            }
            domain()
        }
        trakt {
            data {
                this()
                local()
                remote()
            }
            domain()
        }
    }
    auth {
        tmdb {
            data {
                this()
                local()
                remote()
            }
            domain()
        }
        trakt {
            data {
                this()
                local()
                remote()
            }
            domain()
        }
    }
    common()
    database()
    di {
        kotlin()
    }
    movies {
        data {
            this()
            local()
            remote {
                this()
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
    store()
    suggestions {
        domain()
    }
    test {
        kotlin()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)
    commonMainImplementation(libs.ktor.client.core)
    commonMainImplementation(libs.ktor.client.mock)

    commonTestImplementation(libs.bundles.test.kotlin)
    commonTestImplementation(libs.koin.test)
    commonTestImplementation(libs.koin.test.junit4)
}

kotlin {
    jvm()
}
