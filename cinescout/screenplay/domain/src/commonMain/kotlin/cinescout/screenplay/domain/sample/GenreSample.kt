package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.TmdbGenreId

object GenreSample {

    val Action = Genre(
        id = TmdbGenreId(28),
        name = "Action"
    )

    val Adventure = Genre(
        id = TmdbGenreId(12),
        name = "Adventure"
    )

    val Comedy = Genre(
        id = TmdbGenreId(35),
        name = "Comedy"
    )

    val Crime = Genre(
        id = TmdbGenreId(80),
        name = "Crime"
    )

    val Drama = Genre(
        id = TmdbGenreId(18),
        name = "Drama"
    )

    val Mystery = Genre(
        id = TmdbGenreId(9_648),
        name = "Mystery"
    )

    val ScienceFiction = Genre(
        id = TmdbGenreId(878),
        name = "Science Fiction"
    )

    val SciFiFantasy = Genre(
        id = TmdbGenreId(10_765),
        name = "Sci-Fi & Fantasy"
    )

    val Thriller = Genre(
        id = TmdbGenreId(53),
        name = "Thriller"
    )
}
