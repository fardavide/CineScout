package movies.remote.tmdb

import Actor
import FiveYearRange
import Genre
import IntId
import Name
import movies.Movie
import movies.remote.TmdbRemoteMovieSource
import movies.remote.tmdb.movie.MovieDiscoverService
import movies.remote.tmdb.movie.MovieService

internal class TmdbRemoteMovieSourceImpl(
    private val movieService: MovieService,
    private val movieDiscoverService: MovieDiscoverService
) : TmdbRemoteMovieSource {

    override suspend fun discover(
        actors: Collection<Actor>,
        genres: Collection<Genre>,
        years: FiveYearRange?,
    ): Collection<Movie> {
        val result = movieDiscoverService.discover(actors, genres, years)
        return result.results.map { movieService.details(it.id) }.map { movieModel ->
            Movie(
                name = Name(movieModel.originalTitle),
                genres = movieModel.genres.map { genre -> Genre(id = IntId(genre.id), name = Name(genre.name)) },
                actors = movieModel.credits.cast.map { castPerson ->
                    Actor(
                        id = IntId(castPerson.id),
                        name = Name(castPerson.name)
                    )
                },
                year = 0u // TODO DateTime.parse(movieModel.releaseDate).yearInt.toUInt()
            )
        }
    }

    override suspend fun search(query: String): Collection<Movie> {
        TODO("Not yet implemented")
    }
}
