package cinescout.progress.domain.model

import cinescout.screenplay.domain.model.Movie

sealed interface MovieProgress : ScreenplayProgress {

    override val screenplay: Movie

    data class Unwatched(override val screenplay: Movie) : MovieProgress

    data class Watched(
        val count: Int,
        override val screenplay: Movie
    ) : MovieProgress
}
