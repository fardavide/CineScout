package stats.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrDefault
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
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
import entities.CommunityRating
import entities.FiveYearRange
import entities.Genre
import entities.IntId
import entities.ImageUrl
import entities.UserRating
import entities.TmdbId
import entities.Video
import entities.movies.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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

    override suspend fun ratedMovies(): Collection<Pair<Movie, UserRating>> =
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
                // All Videos per Movie
                val videos = dtos1.groupBy { it.videoTmdbId }.mapNotNull { (videoTmdbId, dtos2) ->
                    videoTmdbId ?: return@mapNotNull null
                    val dto = dtos2.first()
                    Video(
                        videoTmdbId,
                        dto.videoName!!,
                        dto.videoSite!!,
                        dto.videoKey!!,
                        dto.videoType!!,
                        dto.videoSize!!.toUInt()
                    )
                }

                Movie(
                    id = movieParams.tmdbId,
                    name = movieParams.title,
                    poster = movieParams.posterPath?.let { ImageUrl(movieParams.imageBaseUrl!!, it) },
                    backdrop = movieParams.backdropPath?.let { ImageUrl(movieParams.imageBaseUrl!!, it) },
                    actors = actors,
                    genres = genres,
                    year = movieParams.year,
                    rating = CommunityRating(movieParams.voteAverage, movieParams.voteCount.toUInt()),
                    overview = movieParams.overview,
                    videos = videos,
                ) to UserRating(movieParams.rating)
            }

    override fun rating(movie: Movie): Flow<UserRating> =
        movies.selectMovieRatingByTmdbId(movie.id)
            .asFlow().mapToOneOrDefault(0)
            .map { UserRating(it) }
            .distinctUntilChanged()

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
                // All Videos per Movie
                val videos = dtos1.groupBy { it.videoTmdbId }.mapNotNull { (videoTmdbId, dtos2) ->
                    videoTmdbId ?: return@mapNotNull null
                    val dto = dtos2.first()
                    Video(
                        videoTmdbId,
                        dto.videoName!!,
                        dto.videoSite!!,
                        dto.videoKey!!,
                        dto.videoType!!,
                        dto.videoSize!!.toUInt()
                    )
                }

                Movie(
                    id = movieParams.tmdbId,
                    name = movieParams.title,
                    poster = movieParams.posterPath?.let { ImageUrl(movieParams.imageBaseUrl!!, it) },
                    backdrop = movieParams.backdropPath?.let { ImageUrl(movieParams.imageBaseUrl!!, it) },
                    actors = actors,
                    genres = genres,
                    year = movieParams.year,
                    rating = CommunityRating(movieParams.voteAverage, movieParams.voteCount.toUInt()),
                    overview = movieParams.overview,
                    videos = videos,
                )
            }

    override fun isInWatchlist(movie: Movie): Flow<Boolean> =
        movies.selectInWatchlistByTmdbId(movie.id)
            .asFlow().mapToOneOrNull()
            .map { it != null }
//            .distinctUntilChanged()

    override suspend fun rate(movie: Movie, rating: UserRating) {
        val insertionResult = insertMovieAndRelated(movie)

        // Rate Movie
        runCatching { stats.insert(insertionResult.movieId, StatType.MOVIE, rating.weight) }
            .onFailure { stats.update(rating.weight, StatType.MOVIE, insertionResult.movieId) }

        rateActors(insertionResult.actorsIds, rating)
        rateGenres(insertionResult.genresIds, rating)
        rateYear(insertionResult.yearId, rating)
    }

    // VisibleForTesting
    fun rateActors(ids: Collection<IntId>, rating: UserRating) {
        for (id in ids) {
            val prev = stats.selectActorRating(id).executeAsOneOrNull() ?: 0
            val new = prev + rating.weight
            runCatching { stats.insert(id, StatType.ACTOR, new) }
                .onFailure { stats.update(new, StatType.ACTOR, id) }
        }
    }

    // VisibleForTesting
    fun rateGenres(ids: Collection<IntId>, rating: UserRating) {
        for (id in ids) {
            val prev = stats.selectGenreRating(id).executeAsOneOrNull() ?: 0
            val new = prev + rating.weight
            runCatching { stats.insert(id, StatType.GENRE, new) }
                .onFailure { stats.update(new, StatType.GENRE, id) }
        }
    }

    // VisibleForTesting
    fun rateYear(id: IntId, rating: UserRating) {
        val prev = stats.selectYearRating(id).executeAsOneOrNull() ?: 0
        val new = prev + rating.weight
        runCatching { stats.insert(id, StatType.FIVE_YEAR_RANGE, new) }
            .onFailure { stats.update(new, StatType.FIVE_YEAR_RANGE, id) }
    }

    override suspend fun addToWatchlist(movie: Movie) {
        val (movieId) = insertMovieAndRelated(movie)
        runCatching { watchlist.insert(movieId) }
    }

    override suspend fun removeFromWatchlist(movie: Movie) {
        movies.selectIdByTmdbId(movie.id).executeAsOneOrNull()?.let {
            watchlist.deleteByMovieId(it)
        }
    }

    private fun insertMovieAndRelated(movie: Movie): MovieInsertionResult {

        // Insert Movie
        with(movie) {
            runCatching {
                movies.insert(
                    tmdbId = id,
                    title = name,
                    year = year,
                    imageBaseUrl = poster?.baseUrl,
                    posterPath = poster?.path,
                    backdropPath = backdrop?.path,
                    voteAverage = rating.average,
                    voteCount = rating.count.toLong(),
                    overview = overview
                )
            }.onFailure { movies.update(name, year, id) }
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
