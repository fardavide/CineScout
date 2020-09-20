package stats.local

import database.movies.ActorQueries
import database.movies.GenreQueries
import database.movies.MovieQueries
import database.movies.Movie_actorQueries
import database.movies.Movie_genreQueries
import database.movies.YearRangeQueries
import database.stats.StatQueries
import database.stats.StatType
import database.stats.WatchlistQueries
import entities.Actor
import entities.FiveYearRange
import entities.Genre
import entities.IntId
import entities.Poster
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
    private val years: YearRangeQueries,
    private val watchlist: WatchlistQueries,
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
                    movieParams.posterPath?.let { Poster(movieParams.posterBaseUrl!!, it) },
                    actors,
                    genres,
                    movieParams.year
                ) to Rating(movieParams.rating)
            }

    override suspend fun watchlist(): Collection<Movie> =
        movies.selectAllInWatchlist().executeAsList()
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
                    movieParams.posterPath?.let { Poster(movieParams.posterBaseUrl!!, it) },
                    actors,
                    genres,
                    movieParams.year
                )
            }

    override suspend fun rate(movie: Movie, rating: Rating) {
        val insertionResult = insertMovieAndRelated(movie)

        // Rate Movie
        runCatching { stats.insert(insertionResult.movieId, StatType.MOVIE, rating.weight) }
            .onFailure { stats.update(rating.weight, StatType.MOVIE, insertionResult.movieId) }

        rateActors(insertionResult.actorsIds, rating)
        rateGenres(insertionResult.genresIds, rating)
        rateYear(insertionResult.yearId, rating)
    }

    // VisibleForTesting
    fun rateActors(ids: Collection<IntId>, rating: Rating) {
        for (id in ids) {
            val prev = stats.selectActorRating(id).executeAsOneOrNull() ?: 0
            val new = prev + rating.weight
            runCatching { stats.insert(id, StatType.ACTOR, new) }
                .onFailure { stats.update(new, StatType.ACTOR, id) }
        }
    }

    // VisibleForTesting
    fun rateGenres(ids: Collection<IntId>, rating: Rating) {
        for (id in ids) {
            val prev = stats.selectGenreRating(id).executeAsOneOrNull() ?: 0
            val new = prev + rating.weight
            runCatching { stats.insert(id, StatType.GENRE, new) }
                .onFailure { stats.update(new, StatType.GENRE, id) }
        }
    }

    // VisibleForTesting
    fun rateYear(id: IntId, rating: Rating) {
        val prev = stats.selectYearRating(id).executeAsOneOrNull() ?: 0
        val new = prev + rating.weight
        runCatching { stats.insert(id, StatType.FIVE_YEAR_RANGE, new) }
            .onFailure { stats.update(new, StatType.FIVE_YEAR_RANGE, id) }
    }

    override suspend fun addToWatchlist(movie: Movie) {
        val (movieId) = insertMovieAndRelated(movie)
        watchlist.insert(movieId)
    }

    private fun insertMovieAndRelated(movie: Movie): MovieInsertionResult {

        // Insert Movie
        with(movie) {
            runCatching { movies.insert(id, name, year, poster?.baseUrl, poster?.path) }
                .onFailure { movies.update(name, year, id) }
        }
        val movieId = movies.selectIdByTmdbId(movie.id).executeAsOne()

        // Insert Actors
        val actorsIds = movie.actors.map { actor ->
            val id = insertActor(actor)
            // Insert Actor for Movie
            runCatching { movieActors.insert(movieId, id) }
            id
        }

        // Insert Genres
        val genresIds = movie.genres.map { genre ->
            val id = insertGenre((genre))
            runCatching { movieGenres.insert(movieId, id) }
            id
        }

        // Insert Year
        val yearId = insertYear(movie.year)

        return MovieInsertionResult(
            movieId = movieId,
            actorsIds = actorsIds,
            genresIds = genresIds,
            yearId = yearId
        )
    }

    // VisibleForTesting
    fun insertActor(actor: Actor): IntId {
        runCatching { actors.insert(actor.id, actor.name) }
            .onFailure { actors.update(actor.name, actor.id) }

        return actors.selectIdByTmdbId(actor.id).executeAsOne()
    }

    // VisibleForTesting
    fun insertGenre(genre: Genre): IntId {
        runCatching { genres.insert(genre.id, genre.name) }
            .onFailure { genres.update(genre.name, genre.id) }

        return genres.selectIdByTmdbId(genre.id).executeAsOne()
    }

    // VisibleForTesting
    fun insertYear(year: UInt): IntId {
        val yearForRange = FiveYearRange(forYear = year).range.last
        runCatching { years.insert(yearForRange) }

        return IntId(yearForRange.toInt())
    }

    private data class MovieInsertionResult(
        val movieId: IntId,
        val actorsIds: Collection<IntId>,
        val genresIds: Collection<IntId>,
        val yearId: IntId
    )

}
