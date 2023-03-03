package cinescout.suggestions.data.local.mapper

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import cinescout.database.FindAllSuggested
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseSuggestedMovie
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestedMovieSample
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.suggestions.domain.model.Affinity
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import org.koin.core.annotation.Factory

interface DatabaseSuggestedMovieMapper {

    fun toDomainModel(findAllSuggested: FindAllSuggested): SuggestedMovie

    fun toDomainModels(findAllSuggested: List<FindAllSuggested>): List<SuggestedMovie> =
        findAllSuggested.map(::toDomainModel)

    fun toDatabaseModel(suggestedMovie: SuggestedMovie): Pair<DatabaseMovie, DatabaseSuggestedMovie>
}

@Factory
class RealDatabaseSuggestedMovieMapper(
    private val databaseMovieMapper: DatabaseMovieMapper,
    private val databaseSuggestionSourceMapper: DatabaseSuggestionSourceMapper
) : DatabaseSuggestedMovieMapper {

    override fun toDomainModel(findAllSuggested: FindAllSuggested) = SuggestedMovie(
        affinity = Affinity.of(findAllSuggested.affinity.toInt()),
        movie = databaseMovieMapper.toMovie(
            backdropPath = findAllSuggested.backdropPath,
            overview = findAllSuggested.overview,
            posterPath = findAllSuggested.posterPath,
            ratingCount = findAllSuggested.ratingCount,
            ratingAverage = findAllSuggested.ratingAverage,
            releaseDate = findAllSuggested.releaseDate,
            title = findAllSuggested.title,
            tmdbId = findAllSuggested.tmdbId
        ),
        source = databaseSuggestionSourceMapper.toDomainModel(findAllSuggested.source)
    )

    override fun toDatabaseModel(
        suggestedMovie: SuggestedMovie
    ): Pair<DatabaseMovie, DatabaseSuggestedMovie> = Pair(
        databaseMovieMapper.toDatabaseMovie(suggestedMovie.movie),
        DatabaseSuggestedMovie(
            affinity = suggestedMovie.affinity.value.toDouble(),
            source = databaseSuggestionSourceMapper.toDatabaseModel(suggestedMovie.source),
            tmdbId = suggestedMovie.movie.tmdbId.toDatabaseId()
        )
    )
}

class FakeDatabaseSuggestedMovieMapper(
    private val databaseMovie: DatabaseMovie = DatabaseMovieSample.Inception,
    private val databaseSuggestedMovie: DatabaseSuggestedMovie = DatabaseSuggestedMovieSample.Inception,
    private val domainSuggestedMovies: Nel<SuggestedMovie> = nonEmptyListOf(SuggestedMovieSample.Inception)
) : DatabaseSuggestedMovieMapper {

    override fun toDomainModel(findAllSuggested: FindAllSuggested) = domainSuggestedMovies.head

    override fun toDomainModels(findAllSuggested: List<FindAllSuggested>) = domainSuggestedMovies

    override fun toDatabaseModel(suggestedMovie: SuggestedMovie) = databaseMovie to databaseSuggestedMovie
}
