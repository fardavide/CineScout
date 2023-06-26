package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TmdbTvShowId

object TmdbScreenplay {

    const val Id = "id"
}

object TmdbScreenplayType {

    const val Movie = "movie"
    const val TvShow = "tv"
}

fun TmdbScreenplayId.toTmdbScreenplayType() = when (this) {
    is TmdbMovieId -> TmdbScreenplayType.Movie
    is TmdbTvShowId -> TmdbScreenplayType.TvShow
}
