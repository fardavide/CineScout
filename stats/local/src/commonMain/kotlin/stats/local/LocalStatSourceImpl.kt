package stats.local

import com.squareup.sqldelight.Query
import database.stats.*
import entities.*
import entities.Actor
import entities.Genre
import entities.movies.Movie
import stats.LocalStatSource

internal class LocalStatSourceImpl (
    private val actors: ActorQueries,
    private val genres: GenreQueries,
    private val movies: MovieQueries,
    private val movieActors: Movie_actorQueries,
    private val movieGenres: Movie_genreQueries,
    private val stats: StatQueries,
    private val years: YearRangeQueries
) : LocalStatSource {

    override suspend fun topActors(limit: UInt): Collection<Actor> =
        actors.selectTop(limit.toLong()) { id, tmdbId, name, rating ->
            Actor(tmdbId, name)
        }.executeAsList()


    override suspend fun topGenres(limit: UInt): Collection<Genre> =
        genres.selectTop(limit.toLong()) { id, tmdbId, name, rating ->
            Genre(tmdbId, name)
        }.executeAsList()

    override suspend fun topYears(limit: UInt): Collection<FiveYearRange> {
        return years.selectTop(limit.toLong()) { year, rating ->
            FiveYearRange(year)
        }.executeAsList()
    }

    override suspend fun ratedMovies(): Collection<Pair<Movie, Rating>> =
        movies.selectAllRated().executeAsList()
            // All Movies
            .groupBy { it.id }.map { (_, dtos1) ->
                val movieParams = dtos1.first()

                // All Actors per Movie
                val actors = dtos1.groupBy { it.actorTmdbId }.map { (actorTmdbId, dtos2) ->
                    Actor(actorTmdbId, dtos2.first().actorName)
                }
                // All Genres per Movie
                val genres = dtos1.groupBy { it.genreTmdbId }.map { (genreTmdbId, dtos2) ->
                    Genre(genreTmdbId, dtos2.first().genreName)
                }

                Movie(
                    movieParams.tmdbId,
                    movieParams.title,
                    actors,
                    genres,
                    movieParams.year
                ) to Rating(movieParams.rating)
            }


    override suspend fun rate(movie: Movie, rating: Rating) {
        with(movie) {
            // Insert Movie
            movies.insert(id, name, year)
            val id = movies.lastInsertRowId().toIntId()
            stats.insert(id, StatType.MOVIE, rating.weight)

            // Rate Actors
            val actorsIds = rateActors(actors, rating)

            // Insert Actors for Movie
            for (actorId in actorsIds)
                movieActors.insert(id, actorId)

            // Rate Genres
            val genresIds = rateGenres(genres, rating)

            // Insert Genres for Movie
            for (genreId in genresIds)
                movieGenres.insert(id, genreId)

            // Rate Year
            rateYear(FiveYearRange(forYear = year), rating)
        }
    }

    private fun rateActors(_actors: Collection<Actor>, rating: Rating): Collection<IntId> {
        return _actors.map {
            // Insert
            actors.insert(it.id, it.name)
            val id = actors.lastInsertRowId().toIntId()

            // Rate
            val prev = stats.selectActorRating(id).executeAsOne()
            stats.insert(id, StatType.ACTOR, prev + rating.weight)

            id
        }
    }

    private fun rateGenres(_genres: Collection<Genre>, rating: Rating): Collection<IntId> {
        return _genres.map {
            // Insert
            genres.insert(it.id, it.name)
            val id = genres.lastInsertRowId().toIntId()

            // Rate
            val prev = stats.selectGenreRating(id).executeAsOne()
            stats.insert(id, StatType.GENRE, prev + rating.weight)

            id
        }
    }

    private fun rateYear(year: FiveYearRange, rating: Rating) {
        // Insert
        years.insert(year.range.last)
        val id = years.lastInsertRowId().toIntId()

        // Rate
        val prev = stats.selectYearRating(id).executeAsOne()
        stats.insert(id, StatType.FIVE_YEAR_RANGE, prev + rating.weight)
    }

    private fun Query<Long>.toIntId() = IntId(executeAsOne().toInt())
}
