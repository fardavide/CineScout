package cinescout.database.model

import cinescout.database.model.id.DatabaseTmdbMovieId
import cinescout.database.model.id.DatabaseTmdbTvShowId

typealias DatabaseScreenplay = cinescout.database.Screenplay

fun getDataBaseScreenplayType(
    movieId: DatabaseTmdbMovieId?,
    tvShowId: DatabaseTmdbTvShowId?
): DatabaseScreenplayType {
    return when {
        movieId != null -> DatabaseScreenplayType.Movie
        tvShowId != null -> DatabaseScreenplayType.TvShow
        else -> error("Expected either a movie id or a tv show id, but got neither")
    }
}

enum class DatabaseScreenplayType {
    Movie,
    TvShow
}
