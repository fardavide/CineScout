package cinescout.movies.data.remote.tmdb.mapper

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.model.Movie

class TmdbMovieMapper {

    fun toMovie(tmdbMovie: TmdbMovie) = Movie(title = tmdbMovie.title)
}
