package cinescout.suggestions.data.local.mapper

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseSuggestion
import cinescout.database.model.DatabaseTvShow
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestionSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.database.tvShow.FindAllSuggested
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.toScreenplayDatabaseId
import cinescout.suggestions.domain.model.Affinity
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.sample.SuggestedMovieSample
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import cinescout.tvshows.data.local.mapper.DatabaseTvShowMapper
import cinescout.tvshows.data.local.mapper.toScreenplayDatabaseId
import org.koin.core.annotation.Factory
import cinescout.database.movie.FindAllSuggested as FindAllSuggestedMovies
import cinescout.database.tvShow.FindAllSuggested as FindAllSuggestedTvShows

interface DatabaseSuggestionMapper {

    fun toDomainModel(findAllSuggested: FindAllSuggestedMovies): SuggestedMovie

    fun toDomainModel(findAllSuggested: FindAllSuggestedTvShows): SuggestedTvShow

    fun toMovieDomainModels(findAllSuggested: List<FindAllSuggestedMovies>): List<SuggestedMovie> =
        findAllSuggested.map(::toDomainModel)

    fun toTvShowDomainModels(findAllSuggested: List<FindAllSuggestedTvShows>): List<SuggestedTvShow> =
        findAllSuggested.map(::toDomainModel)

    fun toDatabaseModel(suggestedMovie: SuggestedMovie): Pair<DatabaseMovie, DatabaseSuggestion>

    fun toDatabaseModel(suggestedTvShow: SuggestedTvShow): Pair<DatabaseTvShow, DatabaseSuggestion>
}

@Factory
class RealDatabaseSuggestionMapper(
    private val databaseMovieMapper: DatabaseMovieMapper,
    private val databaseTvShowMapper: DatabaseTvShowMapper,
    private val databaseSuggestionSourceMapper: DatabaseSuggestionSourceMapper
) : DatabaseSuggestionMapper {

    override fun toDomainModel(findAllSuggested: FindAllSuggestedMovies) = SuggestedMovie(
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

    override fun toDomainModel(findAllSuggested: FindAllSuggested) = SuggestedTvShow(
        affinity = Affinity.of(findAllSuggested.affinity.toInt()),
        tvShow = databaseTvShowMapper.toTvShow(
            backdropPath = findAllSuggested.backdropPath,
            firstAirDate = findAllSuggested.firstAirDate,
            overview = findAllSuggested.overview,
            posterPath = findAllSuggested.posterPath,
            ratingCount = findAllSuggested.ratingCount,
            ratingAverage = findAllSuggested.ratingAverage,
            title = findAllSuggested.title,
            tmdbId = findAllSuggested.tmdbId
        ),
        source = databaseSuggestionSourceMapper.toDomainModel(findAllSuggested.source)
    )

    override fun toDatabaseModel(suggestedMovie: SuggestedMovie): Pair<DatabaseMovie, DatabaseSuggestion> =
        Pair(
            databaseMovieMapper.toDatabaseMovie(suggestedMovie.movie),
            DatabaseSuggestion(
                affinity = suggestedMovie.affinity.value.toDouble(),
                source = databaseSuggestionSourceMapper.toDatabaseModel(suggestedMovie.source),
                tmdbId = suggestedMovie.movie.tmdbId.toScreenplayDatabaseId()
            )
        )

    override fun toDatabaseModel(suggestedTvShow: SuggestedTvShow): Pair<DatabaseTvShow, DatabaseSuggestion> =
        Pair(
            databaseTvShowMapper.toDatabaseTvShow(suggestedTvShow.tvShow),
            DatabaseSuggestion(
                affinity = suggestedTvShow.affinity.value.toDouble(),
                source = databaseSuggestionSourceMapper.toDatabaseModel(suggestedTvShow.source),
                tmdbId = suggestedTvShow.tvShow.tmdbId.toScreenplayDatabaseId()
            )
        )
}

class FakeDatabaseSuggestionMapper(
    private val databaseMovie: DatabaseMovie = DatabaseMovieSample.Inception,
    private val databaseTvShow: DatabaseTvShow = DatabaseTvShowSample.BreakingBad,
    private val databaseSuggestedMovie: DatabaseSuggestion = DatabaseSuggestionSample.Inception,
    private val databaseSuggestedTvShow: DatabaseSuggestion = DatabaseSuggestionSample.BreakingBad,
    private val domainSuggestedMovies: Nel<SuggestedMovie> = nonEmptyListOf(SuggestedMovieSample.Inception),
    private val domainSuggestedTvShows: Nel<SuggestedTvShow> = nonEmptyListOf(SuggestedTvShowSample.BreakingBad)
) : DatabaseSuggestionMapper {

    override fun toDomainModel(findAllSuggested: FindAllSuggestedMovies) = domainSuggestedMovies.head
    override fun toDomainModel(findAllSuggested: FindAllSuggested) = domainSuggestedTvShows.head

    override fun toMovieDomainModels(findAllSuggested: List<FindAllSuggestedMovies>) = domainSuggestedMovies
    override fun toTvShowDomainModels(findAllSuggested: List<FindAllSuggested>) = domainSuggestedTvShows

    override fun toDatabaseModel(suggestedMovie: SuggestedMovie) = databaseMovie to databaseSuggestedMovie
    override fun toDatabaseModel(suggestedTvShow: SuggestedTvShow) = databaseTvShow to databaseSuggestedTvShow
}
