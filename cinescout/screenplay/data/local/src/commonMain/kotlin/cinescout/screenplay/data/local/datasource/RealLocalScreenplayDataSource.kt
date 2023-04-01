package cinescout.screenplay.data.local.datasource

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cinescout.database.GenreQueries
import cinescout.database.KeywordQueries
import cinescout.database.MovieQueries
import cinescout.database.RecommendationQueries
import cinescout.database.ScreenplayGenreQueries
import cinescout.database.ScreenplayKeywordQueries
import cinescout.database.ScreenplayQueries
import cinescout.database.SimilarQueries
import cinescout.database.TvShowQueries
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainId
import cinescout.screenplay.data.local.mapper.toStringDatabaseId
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Keyword
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class RealLocalScreenplayDataSource(
    private val databaseScreenplayMapper: DatabaseScreenplayMapper,
    private val genreQueries: GenreQueries,
    private val keywordQueries: KeywordQueries,
    private val movieQueries: MovieQueries,
    @IoDispatcher private val readDispatcher: CoroutineDispatcher,
    private val recommendationQueries: RecommendationQueries,
    private val screenplayKeywordQueries: ScreenplayKeywordQueries,
    private val screenplayGenreQueries: ScreenplayGenreQueries,
    private val screenplayQueries: ScreenplayQueries,
    private val similarQueries: SimilarQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @DatabaseWriteDispatcher private val writeDispatcher: CoroutineDispatcher
) : LocalScreenplayDataSource {

    override fun findRecommended(): Flow<List<Screenplay>> =
        screenplayQueries.findAllRecommended(databaseScreenplayMapper::toScreenplay)
            .asFlow()
            .mapToList(readDispatcher)

    override fun findRecommendedIds(): Flow<List<TmdbScreenplayId>> = recommendationQueries.findAll()
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map { it.toDomainId() } }

    override fun findScreenplay(id: TmdbScreenplayId): Flow<Screenplay?> = when (id) {
        is TmdbScreenplayId.Movie ->
            screenplayQueries.findByMovieId(id.toStringDatabaseId(), databaseScreenplayMapper::toScreenplay)
        is TmdbScreenplayId.TvShow ->
            screenplayQueries.findByTvShowId(id.toStringDatabaseId(), databaseScreenplayMapper::toScreenplay)
    }
        .asFlow()
        .mapToOneOrNull(readDispatcher)

    override fun findScreenplayGenres(id: TmdbScreenplayId): Flow<ScreenplayGenres?> =
        genreQueries.findAllByScreenplayId(id.toDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                val genres = list.map { databaseGenre ->
                    Genre(id = databaseGenre.tmdbId.toDomainId(), name = databaseGenre.name)
                }
                ScreenplayGenres(genres = genres, screenplayId = id).takeIf { list.isNotEmpty() }
            }

    override fun findScreenplayKeywords(id: TmdbScreenplayId): Flow<ScreenplayKeywords?> =
        keywordQueries.findAllByScreenplayId(id.toDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                val keywords = list.map { databaseKeyword ->
                    Keyword(id = databaseKeyword.keywordId.toDomainId(), name = databaseKeyword.name)
                }
                ScreenplayKeywords(keywords = keywords, screenplayId = id).takeIf { list.isNotEmpty() }
            }

    override fun findSimilar(id: TmdbScreenplayId): Flow<List<Screenplay>> =
        screenplayQueries.findSimilar(id.toDatabaseId(), databaseScreenplayMapper::toScreenplay)
            .asFlow()
            .mapToList(readDispatcher)

    override suspend fun insert(screenplay: Screenplay) {
        transacter.suspendTransaction(writeDispatcher) {
            when (screenplay) {
                is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
            }
        }
    }

    override suspend fun insert(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }

    override suspend fun insertRecommended(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                recommendationQueries.insert(screenplay.tmdbId.toDatabaseId())
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }

    override suspend fun insertRecommendedIds(ids: List<TmdbScreenplayId>) {
        recommendationQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                recommendationQueries.insert(id.toDatabaseId())
            }
        }
    }

    override suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres) {
        transacter.suspendTransaction(writeDispatcher) {
            for (genre in screenplayGenres.genres) {
                screenplayGenreQueries.insert(
                    screenplayId = screenplayGenres.screenplayId.toDatabaseId(),
                    genreId = genre.id.toDatabaseId()
                )
                genreQueries.insertGenre(
                    tmdbId = genre.id.toDatabaseId(),
                    name = genre.name
                )
            }
        }
    }

    override suspend fun insertScreenplayKeywords(screenplayKeywords: ScreenplayKeywords) {
        transacter.suspendTransaction(writeDispatcher) {
            for (keyword in screenplayKeywords.keywords) {
                screenplayKeywordQueries.insertKeyword(
                    screenplayId = screenplayKeywords.screenplayId.toDatabaseId(),
                    keywordId = keyword.id.toDatabaseId()
                )
                keywordQueries.insertKeyword(
                    tmdbId = keyword.id.toDatabaseId(),
                    name = keyword.name
                )
            }
        }
    }

    override suspend fun insertSimilar(id: TmdbScreenplayId, screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                similarQueries.insert(id.toDatabaseId(), screenplay.tmdbId.toDatabaseId())
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }
}
