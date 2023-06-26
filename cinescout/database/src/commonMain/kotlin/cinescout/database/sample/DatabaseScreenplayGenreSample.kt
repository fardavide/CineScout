package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplayGenre
import cinescout.database.model.id.DatabaseGenreSlug

object DatabaseScreenplayGenreSample {

    val Action = DatabaseScreenplayGenre(
        genreSlug = DatabaseGenreSlug("action"),
        screenplayId = DatabaseMovieSample.Inception.traktId
    )

    val Adventure = DatabaseScreenplayGenre(
        genreSlug = DatabaseGenreSlug("adventure"),
        screenplayId = DatabaseMovieSample.Inception.traktId
    )

    val ScienceFiction = DatabaseScreenplayGenre(
        genreSlug = DatabaseGenreSlug("science-fiction"),
        screenplayId = DatabaseMovieSample.Inception.traktId
    )
}
