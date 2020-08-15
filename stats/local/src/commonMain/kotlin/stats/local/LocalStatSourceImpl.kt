package stats.local

import database.stats.ActorQueries
import database.stats.GenreQueries
import database.stats.MovieQueries
import database.stats.Movie_actorQueries
import database.stats.Movie_genreQueries
import database.stats.StatQueries
import database.stats.StatType
import database.stats.YearRangeQueries
import entities.Actor
import entities.FiveYearRange
import entities.Genre
import entities.IntId
import entities.Rating
import entities.TmdbId
import entities.movies.Movie
import stats.LocalStatSource

@Suppress("RedundantSuspendModifier")
internal class LocalStatSourceImpl (
    private val actors: ActorQueries,
    private val genres: GenreQueries,
    private val movies: MovieQueries,
    private val movieActors: Movie_actorQueries,
    private val movieGenres: Movie_genreQueries,
    private val stats: StatQueries,
    private val years: YearRangeQueries
) : LocalStatSource {

    suspend fun actorRating(id: TmdbId): Int? =
        stats.selectActorRatingByTmdbId(id).executeAsOneOrNull()

    override suspend fun topActors(limit: UInt): Collection<Actor> =
        actors.selectTop(limit.toLong()) { id, tmdbId, name, rating ->
            Actor(tmdbId, name)
        }.executeAsList()

    suspend fun genreRating(id: TmdbId): Int? =
        stats.selectGenreRatingByTmdbId(id).executeAsOneOrNull()

    override suspend fun topGenres(limit: UInt): Collection<Genre> =
        genres.selectTop(limit.toLong()) { id, tmdbId, name, rating ->
            Genre(tmdbId, name)
        }.executeAsList()

    suspend fun yearRating(range: FiveYearRange): Int? =
        stats.selectYearRatingById(range.range.last).executeAsOneOrNull()

    override suspend fun topYears(limit: UInt): Collection<FiveYearRange> {
        return years.selectTop(limit.toLong()) { year, rating ->
            FiveYearRange(year)
        }.executeAsList()
    }

    suspend fun movieRating(id: TmdbId): Int? =
        stats.selectMovieRatingByTmdbId(id).executeAsOneOrNull()

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
            runCatching { movies.insert(id, name, year) }
                .onFailure { movies.update(name, year, id) }
            val id = movies.selectIdByTmdbId(id).executeAsOne()
            runCatching{ stats.insert(id, StatType.MOVIE, rating.weight) }
                .onFailure { stats.update(rating.weight, StatType.MOVIE, id) }

            // Rate Actors
            val actorsIds = rateActors(actors, rating)

            // Insert Actors for Movie
            for (actorId in actorsIds)
                runCatching { movieActors.insert(id, actorId) }

            // Rate Genres
            val genresIds = rateGenres(genres, rating)

            // Insert Genres for Movie
            for (genreId in genresIds)
                runCatching { movieGenres.insert(id, genreId) }

            // Rate Year
            rateYear(FiveYearRange(forYear = year), rating)
        }
    }

    fun rateActors(_actors: Collection<Actor>, rating: Rating): Collection<IntId> {
        return _actors.map { actor ->
            // Insert
            runCatching { actors.insert(actor.id, actor.name) }
                .onFailure { actors.update(actor.name, actor.id) }
            val id = actors.selectIdByTmdbId(actor.id).executeAsOne()

            // Rate
            val prev = stats.selectActorRating(id).executeAsOneOrNull() ?: 0
            val new = prev + rating.weight
            runCatching { stats.insert(id, StatType.ACTOR, new) }
                .onFailure { stats.update(new, StatType.ACTOR, id) }

            id
        }
    }

    fun rateGenres(_genres: Collection<Genre>, rating: Rating): Collection<IntId> {
        return _genres.map { genre ->
            // Insert
            runCatching { genres.insert(genre.id, genre.name) }
                .onFailure { genres.update(genre.name, genre.id) }
            val id = genres.selectIdByTmdbId(genre.id).executeAsOne()

            // Rate
            val prev = stats.selectGenreRating(id).executeAsOneOrNull() ?: 0
            val new = prev + rating.weight
            runCatching { stats.insert(id, StatType.GENRE, new) }
                .onFailure { stats.update(new, StatType.GENRE, id) }

            id
        }
    }

    fun rateYear(year: FiveYearRange, rating: Rating) {
        // Insert
        runCatching { years.insert(year.range.last) }
        val id = IntId(year.range.last.toInt())

        // Rate
        val prev = stats.selectYearRating(id).executeAsOneOrNull() ?: 0
        val new = prev + rating.weight
        runCatching { stats.insert(id, StatType.FIVE_YEAR_RANGE, new) }
            .onFailure { stats.update(new, StatType.FIVE_YEAR_RANGE, id) }
    }
}
