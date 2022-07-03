@file:Suppress("unused")

val di = Di
object Di

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

val stats = Stats
object Stats {

    val domain = Domain
    object Domain
}

val suggestions = Suggestions
object Suggestions {

    val domain = Domain
    object Domain
}

val test = Test
object Test {

    const val sourceSet = "commonTest"

    val android = Android
    object Android {

        const val sourceSet = "androidTest"

        val instrumented = Instrumented
        object Instrumented
    }

    val kotlin = Kotlin
    object Kotlin
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
