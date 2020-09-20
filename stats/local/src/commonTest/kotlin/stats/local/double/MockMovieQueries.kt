package stats.local.double

import database.movies.Movie
import database.movies.MovieDetails
import database.movies.MovieDetailsWithRating
import database.movies.MovieQueries
import database.stats.StatType
import entities.IntId
import entities.Name
import entities.TmdbId
import io.mockk.every
import io.mockk.mockk

fun mockMovieQueries(
    movies: MutableList<Movie>,
    movieActors: MutableList<Pair<IntId, IntId>>,
    movieGenres: MutableList<Pair<IntId, IntId>>,
    actors: MutableList<Pair<TmdbId, Name>>,
    genres: MutableList<Pair<TmdbId, Name>>,
    stats: MutableList<Triple<IntId, StatType, Int>>,
    watchlist: MutableList<IntId>
): MovieQueries = mockk {

    every {
        insert(
            tmdbId = TmdbId(any()),
            title = Name(any()),
            year = any<Int>().toUInt(),
            posterBaseUrl = any(),
            posterPath = any()
        )
    } answers {
        val idArg = firstArg<Int>()
        val tmdbIdArg = TmdbId(idArg)
        val index = movies.indexOf { it.tmdbId == tmdbIdArg }
        val movie =
            Movie(IntId(idArg), tmdbIdArg, Name(secondArg()), thirdArg<Int>().toUInt(), arg(3), arg(4))
        movies.insert(index, movie)
    }

    every { selectIdByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOne() } answers {
                IntId(movies.indexOf { it.tmdbId == tmdbIdArg }!!)
            }
            every { executeAsOneOrNull() } answers {
                movies.indexOf { it.tmdbId == tmdbIdArg }?.let(::IntId)
            }
        }
    }

    every { selectAllRated() } answers {
        mockk {
            every { executeAsList() } answers {
                movies.flatMapIndexed { index: Int, movie: Movie ->
                    val movieId = IntId(index)

                    val moviesActors =
                        movieActors.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { actors[it.i] }
                    val moviesGenres =
                        movieGenres.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { genres[it.i] }

                    moviesActors.flatMap { actor ->
                        moviesGenres.map { genre ->
                            MovieDetailsWithRating(
                                id = movieId,
                                tmdbId = movie.tmdbId,
                                title = movie.title,
                                year = movie.year,
                                posterBaseUrl = movie.posterBaseUrl,
                                posterPath = movie.posterPath,
                                actorTmdbId = actor.first,
                                actorName = actor.second,
                                genreTmdbId = genre.first,
                                genreName = genre.second,
                                rating = stats.find { (statId, type, _) ->
                                    statId == movieId && type == StatType.MOVIE
                                }?.third ?: 0
                            )
                        }
                    }
                }
            }
        }
    }

    every { selectMovieRatingByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                val id = IntId(movies.indexOf { tmdbIdArg == it.tmdbId }!!)
                stats.find { (statId, type, _) -> statId == id && type == StatType.MOVIE }?.third
            }
        }
    }

    every { selectAllInWatchlist() } answers {
        mockk {
            every { executeAsList() } answers {
                movies.filter { movie ->
                    IntId(movies.indexOf { movie.tmdbId == it.tmdbId }!!) in watchlist
                }.flatMapIndexed { index: Int, movie: Movie ->
                    val movieId = IntId(index)

                    val moviesActors =
                        movieActors.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { actors[it.i] }
                    val moviesGenres =
                        movieGenres.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                            .map { genres[it.i] }

                    moviesActors.flatMap { actor ->
                        moviesGenres.map { genre ->
                            MovieDetails(
                                id = movieId,
                                tmdbId = movie.tmdbId,
                                title = movie.title,
                                year = movie.year,
                                posterBaseUrl = movie.posterBaseUrl,
                                posterPath = movie.posterPath,
                                actorTmdbId = actor.first,
                                actorName = actor.second,
                                genreTmdbId = genre.first,
                                genreName = genre.second
                            )
                        }
                    }
                }
            }
        }
    }

    every { selectInWatchlistByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                val id = IntId(movies.indexOf { tmdbIdArg == it.tmdbId }!!)
                if (id in watchlist) id else null
            }
        }
    }
}
