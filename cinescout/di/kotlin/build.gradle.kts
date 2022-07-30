plugins {
    id("cinescout.kotlin")
}

moduleDependencies {
    account {
        tmdb {
            data {
                this()
//                local()
                remote()
            }
            domain()
        }
//        trakt {
//            data {
//                this()
//                local()
//                remote()
//            }
//            domain()
//        }
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
    database()
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
    suggestions {
        domain()
    }
    utils {
        kotlin()
    }
}

dependencies {
    commonMainImplementation(libs.bundles.base)

    commonTestImplementation(libs.bundles.test.kotlin)
    commonTestImplementation(libs.koin.test)
    commonTestImplementation(libs.koin.test.junit4)
}

kotlin {
    jvm()
}
