package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

object TmdbScreenplay {

    const val Id = "id"
}

object TmdbScreenplayType {

    const val Movie = "movie"
    const val TvShow = "tv"
}

fun TmdbScreenplayId.toTmdbScreenplayType() = when (this) {
    is TmdbScreenplayId.Movie -> TmdbScreenplayType.Movie
    is TmdbScreenplayId.TvShow -> TmdbScreenplayType.TvShow
}
