package movies.remote.tmdb

import entities.Actor
import entities.FiveYearRange
import entities.Genre
import entities.Name
import entities.TmdbId
import entities.movies.Movie
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
                id = TmdbId(0),
                name = Name(movieModel.originalTitle),
                actors = movieModel.credits.cast.map { castPerson ->
                    Actor(
                        id = TmdbId(castPerson.id),
                        name = Name(castPerson.name)
                    )
                },
                genres = movieModel.genres.map { genre -> Genre(id = TmdbId(genre.id), name = Name(genre.name)) },
                year = 0u // TODO DateTime.parse(movieModel.releaseDate).yearInt.toUInt()
            )
        }
    }

    override suspend fun search(query: String): Collection<Movie> {
        TODO("Not yet implemented")
    }
}
