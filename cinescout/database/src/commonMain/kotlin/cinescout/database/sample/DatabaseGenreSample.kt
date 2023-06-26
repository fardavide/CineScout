package cinescout.database.sample

import cinescout.database.model.DatabaseGenre
import cinescout.database.model.id.DatabaseGenreSlug

object DatabaseGenreSample {

    val Action = DatabaseGenre(
        name = "Action",
        slug = DatabaseGenreSlug("action")
    )

    val Adventure = DatabaseGenre(
        name = "Adventure",
        slug = DatabaseGenreSlug("adventure")
    )

    val Comedy = DatabaseGenre(
        name = "Comedy",
        slug = DatabaseGenreSlug("comedy")
    )

    val Crime = DatabaseGenre(
        name = "Crime",
        slug = DatabaseGenreSlug("crime")
    )

    val Drama = DatabaseGenre(
        name = "Drama",
        slug = DatabaseGenreSlug("drama")
    )

    val ScienceFiction = DatabaseGenre(
        name = "Science Fiction",
        slug = DatabaseGenreSlug("science-fiction")
    )
}
