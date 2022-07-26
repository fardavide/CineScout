@file:Suppress("unused")

val auth = Auth
object Auth {

    val tmdb = Tmdb
    object Tmdb {

        val data = Data
        object Data {

            val local = Local
            object Local

            val remote = Remote
            object Remote
        }

        val domain = Domain
        object Domain
    }

    val trakt = Trakt
    object Trakt {

        val data = Data
        object Data {

            val local = Local
            object Local

            val remote = Remote
            object Remote
        }

        val domain = Domain
        object Domain
    }
}

val database = Database
object Database

val design = Design
object Design

val di = Di
object Di

val home = Home
object Home {

    val presentation = Presentation
    object Presentation
}

val movies = Movies
object Movies {

    val data = Data
    object Data {

        val local = Local
        object Local

        val remote = Remote
        object Remote {

            val tmdb = Tmdb
            object Tmdb

            val trakt = Trakt
            object Trakt
        }
    }

    val domain = Domain
    object Domain
}

val network = Network
object Network {

    val tmdb = Tmdb
    object Tmdb

    val trakt = Trakt
    object Trakt
}

val suggestions = Suggestions
object Suggestions {

    val domain = Domain
    object Domain
}

val test = Test
object Test {

    const val sourceSet = "commonTest"

    val compose = Compose
    object Compose {

        const val sourceSet = "androidTest"
    }
}

val utils = Utils
object Utils {

    val android = Android
    object Android {

        val instrumented = Instrumented
        object Instrumented
    }

    val kotlin = Kotlin
    object Kotlin
}
