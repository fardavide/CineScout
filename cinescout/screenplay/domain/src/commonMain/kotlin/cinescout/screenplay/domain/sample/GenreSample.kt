package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.id.GenreSlug

object GenreSample {

    val Action = Genre(
        slug = GenreSlug("action"),
        name = "Action"
    )

    val Adventure = Genre(
        slug = GenreSlug("adventure"),
        name = "Adventure"
    )

    val Comedy = Genre(
        slug = GenreSlug("comedy"),
        name = "Comedy"
    )

    val Crime = Genre(
        slug = GenreSlug("crime"),
        name = "Crime"
    )

    val Drama = Genre(
        slug = GenreSlug("drama"),
        name = "Drama"
    )

    val Fantasy = Genre(
        slug = GenreSlug("fantasy"),
        name = "Fantasy"
    )

    val Mystery = Genre(
        slug = GenreSlug("mystery"),
        name = "Mystery"
    )

    val ScienceFiction = Genre(
        slug = GenreSlug("science-fiction"),
        name = "Science Fiction"
    )

    val Thriller = Genre(
        slug = GenreSlug("thriller"),
        name = "Thriller"
    )
}
