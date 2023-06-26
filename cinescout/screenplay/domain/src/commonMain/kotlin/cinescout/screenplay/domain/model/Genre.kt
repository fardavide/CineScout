package cinescout.screenplay.domain.model

import cinescout.screenplay.domain.model.id.GenreSlug

data class Genre(
    val name: String,
    val slug: GenreSlug
)
