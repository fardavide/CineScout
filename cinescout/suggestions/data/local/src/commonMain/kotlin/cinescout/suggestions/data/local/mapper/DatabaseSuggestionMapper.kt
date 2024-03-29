package cinescout.suggestions.data.local.mapper

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import cinescout.database.ext.ids
import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseSuggestion
import cinescout.database.model.DatabaseTvShow
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseSuggestionSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toMovieDomainIds
import cinescout.screenplay.data.local.mapper.toTvShowDomainIds
import cinescout.suggestions.domain.model.Affinity
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.sample.SuggestedScreenplayIdSample
import org.koin.core.annotation.Factory

interface DatabaseSuggestionMapper {

    fun toSuggestedMovieId(suggestion: DatabaseSuggestion): SuggestedMovieId

    fun toSuggestedMovieIds(suggestions: List<DatabaseSuggestion>): List<SuggestedMovieId> =
        suggestions.map(::toSuggestedMovieId)
    fun toSuggestedTvShowId(suggestion: DatabaseSuggestion): SuggestedTvShowId

    fun toSuggestedTvShowIds(suggestions: List<DatabaseSuggestion>): List<SuggestedTvShowId> =
        suggestions.map(::toSuggestedTvShowId)

    fun toDatabaseModel(suggestedScreenplayId: SuggestedScreenplayId): DatabaseSuggestion

    fun toDatabaseModel(suggestedMovie: SuggestedMovie): Pair<DatabaseMovie, DatabaseSuggestion>

    fun toDatabaseModel(suggestedTvShow: SuggestedTvShow): Pair<DatabaseTvShow, DatabaseSuggestion>
}

@Factory
class RealDatabaseSuggestionMapper(
    private val databaseScreenplayMapper: DatabaseScreenplayMapper,
    private val databaseSuggestionSourceMapper: DatabaseSuggestionSourceMapper
) : DatabaseSuggestionMapper {

    override fun toSuggestedMovieId(suggestion: DatabaseSuggestion) = SuggestedMovieId(
        affinity = Affinity.of(suggestion.affinity.toInt()),
        screenplayIds = suggestion.ids.toMovieDomainIds(),
        source = databaseSuggestionSourceMapper.toDomainModel(suggestion.source)
    )

    override fun toSuggestedTvShowId(suggestion: DatabaseSuggestion) = SuggestedTvShowId(
        affinity = Affinity.of(suggestion.affinity.toInt()),
        screenplayIds = suggestion.ids.toTvShowDomainIds(),
        source = databaseSuggestionSourceMapper.toDomainModel(suggestion.source)
    )

    override fun toDatabaseModel(suggestedScreenplayId: SuggestedScreenplayId): DatabaseSuggestion =
        DatabaseSuggestion(
            affinity = suggestedScreenplayId.affinity.value.toDouble(),
            source = databaseSuggestionSourceMapper.toDatabaseModel(suggestedScreenplayId.source),
            tmdbId = suggestedScreenplayId.screenplayIds.tmdb.toDatabaseId(),
            traktId = suggestedScreenplayId.screenplayIds.trakt.toDatabaseId()
        )

    override fun toDatabaseModel(suggestedMovie: SuggestedMovie): Pair<DatabaseMovie, DatabaseSuggestion> =
        Pair(
            databaseScreenplayMapper.toDatabaseMovie(suggestedMovie.screenplay),
            DatabaseSuggestion(
                affinity = suggestedMovie.affinity.value.toDouble(),
                source = databaseSuggestionSourceMapper.toDatabaseModel(suggestedMovie.source),
                tmdbId = suggestedMovie.screenplay.tmdbId.toDatabaseId(),
                traktId = suggestedMovie.screenplay.traktId.toDatabaseId()
            )
        )

    override fun toDatabaseModel(suggestedTvShow: SuggestedTvShow): Pair<DatabaseTvShow, DatabaseSuggestion> =
        Pair(
            databaseScreenplayMapper.toDatabaseTvShow(suggestedTvShow.screenplay),
            DatabaseSuggestion(
                affinity = suggestedTvShow.affinity.value.toDouble(),
                source = databaseSuggestionSourceMapper.toDatabaseModel(suggestedTvShow.source),
                tmdbId = suggestedTvShow.screenplay.tmdbId.toDatabaseId(),
                traktId = suggestedTvShow.screenplay.traktId.toDatabaseId()
            )
        )
}

class FakeDatabaseSuggestionMapper(
    private val databaseMovie: DatabaseMovie = DatabaseMovieSample.Inception,
    private val databaseTvShow: DatabaseTvShow = DatabaseTvShowSample.BreakingBad,
    private val databaseSuggestedMovie: DatabaseSuggestion = DatabaseSuggestionSample.Inception,
    private val databaseSuggestedTvShow: DatabaseSuggestion = DatabaseSuggestionSample.BreakingBad,
    private val domainSuggestedMovieIds: Nel<SuggestedMovieId> =
        nonEmptyListOf(SuggestedScreenplayIdSample.Inception),
    private val domainSuggestedTvShowIds: Nel<SuggestedTvShowId> =
        nonEmptyListOf(SuggestedScreenplayIdSample.BreakingBad)
) : DatabaseSuggestionMapper {

    override fun toSuggestedMovieId(suggestion: DatabaseSuggestion) = domainSuggestedMovieIds.head

    override fun toSuggestedTvShowId(suggestion: DatabaseSuggestion) = domainSuggestedTvShowIds.head
    override fun toDatabaseModel(suggestedScreenplayId: SuggestedScreenplayId): DatabaseSuggestion =
        when (suggestedScreenplayId) {
            is SuggestedMovieId -> databaseSuggestedMovie
            is SuggestedTvShowId -> databaseSuggestedTvShow
        }

    override fun toDatabaseModel(suggestedMovie: SuggestedMovie) = databaseMovie to databaseSuggestedMovie

    override fun toDatabaseModel(suggestedTvShow: SuggestedTvShow) = databaseTvShow to databaseSuggestedTvShow
}
