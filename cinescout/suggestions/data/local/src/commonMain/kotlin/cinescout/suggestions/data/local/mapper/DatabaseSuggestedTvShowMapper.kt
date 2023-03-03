package cinescout.suggestions.data.local.mapper

import arrow.core.Nel
import arrow.core.nonEmptyListOf
import cinescout.database.model.DatabaseSuggestedTvShow
import cinescout.database.model.DatabaseTvShow
import cinescout.database.sample.DatabaseSuggestedTvShowSample
import cinescout.database.sample.DatabaseTvShowSample
import cinescout.database.tvShow.FindAllSuggested
import cinescout.suggestions.domain.model.Affinity
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.sample.SuggestedTvShowSample
import cinescout.tvshows.data.local.mapper.DatabaseTvShowMapper
import cinescout.tvshows.data.local.mapper.toDatabaseId
import org.koin.core.annotation.Factory

interface DatabaseSuggestedTvShowMapper {

    fun toDomainModel(findAllSuggested: FindAllSuggested): SuggestedTvShow

    fun toDomainModels(findAllSuggested: List<FindAllSuggested>): List<SuggestedTvShow> =
        findAllSuggested.map(::toDomainModel)

    fun toDatabaseModel(suggestedTvShow: SuggestedTvShow): Pair<DatabaseTvShow, DatabaseSuggestedTvShow>
}

@Factory
class RealDatabaseSuggestedTvShowMapper(
    private val databaseTvShowMapper: DatabaseTvShowMapper,
    private val databaseSuggestionSourceMapper: DatabaseSuggestionSourceMapper
) : DatabaseSuggestedTvShowMapper {

    override fun toDomainModel(findAllSuggested: FindAllSuggested) = SuggestedTvShow(
        affinity = Affinity.of(findAllSuggested.affinity.toInt()),
        tvShow = databaseTvShowMapper.toTvShow(
            backdropPath = findAllSuggested.backdropPath,
            overview = findAllSuggested.overview,
            posterPath = findAllSuggested.posterPath,
            ratingCount = findAllSuggested.ratingCount,
            ratingAverage = findAllSuggested.ratingAverage,
            firstAirDate = findAllSuggested.firstAirDate,
            title = findAllSuggested.title,
            tmdbId = findAllSuggested.tmdbId
        ),
        source = databaseSuggestionSourceMapper.toDomainModel(findAllSuggested.source)
    )

    override fun toDatabaseModel(
        suggestedTvShow: SuggestedTvShow
    ): Pair<DatabaseTvShow, DatabaseSuggestedTvShow> = Pair(
        databaseTvShowMapper.toDatabaseTvShow(suggestedTvShow.tvShow),
        DatabaseSuggestedTvShow(
            affinity = suggestedTvShow.affinity.value.toDouble(),
            source = databaseSuggestionSourceMapper.toDatabaseModel(suggestedTvShow.source),
            tmdbId = suggestedTvShow.tvShow.tmdbId.toDatabaseId()
        )
    )
}

class FakeDatabaseSuggestedTvShowMapper(
    private val databaseTvShow: DatabaseTvShow = DatabaseTvShowSample.Grimm,
    private val databaseSuggestedTvShow: DatabaseSuggestedTvShow = DatabaseSuggestedTvShowSample.Grimm,
    private val domainSuggestedTvShows: Nel<SuggestedTvShow> = nonEmptyListOf(SuggestedTvShowSample.Grimm)
) : DatabaseSuggestedTvShowMapper {

    override fun toDomainModel(findAllSuggested: FindAllSuggested) = domainSuggestedTvShows.head

    override fun toDomainModels(findAllSuggested: List<FindAllSuggested>) = domainSuggestedTvShows

    override fun toDatabaseModel(suggestedTvShow: SuggestedTvShow) = databaseTvShow to databaseSuggestedTvShow
}
