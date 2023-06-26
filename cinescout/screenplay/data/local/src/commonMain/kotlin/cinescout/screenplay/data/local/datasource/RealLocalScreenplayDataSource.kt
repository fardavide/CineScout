package cinescout.screenplay.data.local.datasource

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import arrow.core.Nel
import arrow.core.toNonEmptyListOrNull
import cinescout.database.GenreQueries
import cinescout.database.KeywordQueries
import cinescout.database.MovieQueries
import cinescout.database.RecommendationQueries
import cinescout.database.ScreenplayFindWithGenreSlugsQueries
import cinescout.database.ScreenplayGenreQueries
import cinescout.database.ScreenplayKeywordQueries
import cinescout.database.ScreenplayQueries
import cinescout.database.SimilarQueries
import cinescout.database.TvShowQueries
import cinescout.database.ext.ids
import cinescout.database.util.suspendTransaction
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.local.mapper.DatabaseGenreMapper
import cinescout.screenplay.data.local.mapper.DatabaseScreenplayMapper
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.data.local.mapper.toDomainId
import cinescout.screenplay.data.local.mapper.toDomainIds
import cinescout.screenplay.data.local.mapper.toStringDatabaseId
import cinescout.screenplay.data.local.mapper.toTraktDatabaseId
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Keyword
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TraktMovieId
import cinescout.screenplay.domain.model.id.TraktScreenplayId
import cinescout.screenplay.domain.model.id.TraktTvShowId
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalScreenplayDataSource(
    private val databaseScreenplayMapper: DatabaseScreenplayMapper,
    private val genreMapper: DatabaseGenreMapper,
    private val genreQueries: GenreQueries,
    private val keywordQueries: KeywordQueries,
    private val movieQueries: MovieQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val recommendationQueries: RecommendationQueries,
    private val screenplayKeywordQueries: ScreenplayKeywordQueries,
    private val screenplayGenreQueries: ScreenplayGenreQueries,
    private val screenplayQueries: ScreenplayQueries,
    private val screenplayWithGenreSlugsQueries: ScreenplayFindWithGenreSlugsQueries,
    private val similarQueries: SimilarQueries,
    private val transacter: Transacter,
    private val tvShowQueries: TvShowQueries,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalScreenplayDataSource {

    override fun findAllGenres(): Flow<Nel<Genre>?> = genreQueries.findAll(genreMapper::toGenre)
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.toNonEmptyListOrNull() }

    override fun findRecommended(): Flow<List<Screenplay>> =
        screenplayQueries.findAllRecommended(databaseScreenplayMapper::toScreenplay)
            .asFlow()
            .mapToList(readDispatcher)

    override fun findRecommendedIds(): Flow<List<ScreenplayIds>> = recommendationQueries.findAll()
        .asFlow()
        .mapToList(readDispatcher)
        .map { list -> list.map { it.ids.toDomainIds() } }

    override fun findScreenplay(id: TraktScreenplayId): Flow<Screenplay?> = when (id) {
        is TraktMovieId ->
            screenplayQueries.findByTraktMovieId(id.toStringDatabaseId(), databaseScreenplayMapper::toScreenplay)
        is TraktTvShowId ->
            screenplayQueries.findByTraktTvShowId(id.toStringDatabaseId(), databaseScreenplayMapper::toScreenplay)
    }
        .asFlow()
        .mapToOneOrNull(readDispatcher)

    override fun findScreenplayGenres(id: ScreenplayIds): Flow<ScreenplayGenres?> =
        genreQueries.findAllBySlug(id.toTraktDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                val genres = list.map { databaseGenre ->
                    Genre(slug = databaseGenre.slug.toDomainId(), name = databaseGenre.name)
                }
                ScreenplayGenres(genres = genres, screenplayIds = id).takeIf { list.isNotEmpty() }
            }

    override fun findScreenplayKeywords(id: TmdbScreenplayId): Flow<ScreenplayKeywords?> =
        keywordQueries.findAllByScreenplayId(id.toDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                val keywords = list.map { databaseKeyword ->
                    Keyword(id = databaseKeyword.keywordId.toDomainId(), name = databaseKeyword.name)
                }
                ScreenplayKeywords(keywords = keywords, screenplayId = id).takeIf {
                    list.isNotEmpty()
                }
            }

    override fun findScreenplayWithGenreSlugs(ids: ScreenplayIds): Flow<ScreenplayWithGenreSlugs?> =
        screenplayWithGenreSlugsQueries.byTraktId(ids.trakt.toStringDatabaseId())
            .asFlow()
            .mapToList(readDispatcher)
            .map(databaseScreenplayMapper::toScreenplayWithGenreSlugs)

    override fun findSimilar(ids: ScreenplayIds): Flow<List<Screenplay>> =
        screenplayQueries.findSimilar(ids.tmdb.toDatabaseId(), databaseScreenplayMapper::toScreenplay)
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

    override suspend fun insertGenres(genres: Nel<Genre>) {
        genreQueries.suspendTransaction(writeDispatcher) {
            for (genre in genres) {
                genreQueries.insertGenre(
                    name = genre.name,
                    slug = genre.slug.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertScreenplayGenres(screenplayGenres: ScreenplayGenres) {
        transacter.suspendTransaction(writeDispatcher) {
            for (genre in screenplayGenres.genres) {
                screenplayGenreQueries.insert(
                    genreSlug = genre.slug.toDatabaseId(),
                    screenplayId = screenplayGenres.screenplayIds.toTraktDatabaseId()
                )
                genreQueries.insertGenre(
                    name = genre.name,
                    slug = genre.slug.toDatabaseId()
                )
            }
        }
    }

    override suspend fun insertRecommended(screenplays: List<Screenplay>) {
        transacter.suspendTransaction(writeDispatcher) {
            for (screenplay in screenplays) {
                recommendationQueries.insert(screenplay.traktId.toDatabaseId(), screenplay.tmdbId.toDatabaseId())
                when (screenplay) {
                    is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                    is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
                }
            }
        }
    }

    override suspend fun insertRecommendedIds(ids: List<ScreenplayIds>) {
        recommendationQueries.suspendTransaction(writeDispatcher) {
            for (id in ids) {
                recommendationQueries.insert(id.trakt.toDatabaseId(), id.tmdb.toDatabaseId())
            }
        }
    }

    override suspend fun insertScreenplayWithGenreSlugs(screenplayWithGenreSlugs: ScreenplayWithGenreSlugs) {
        transacter.suspendTransaction(writeDispatcher) {
            when (val screenplay = screenplayWithGenreSlugs.screenplay) {
                is Movie -> movieQueries.insertMovieObject(databaseScreenplayMapper.toDatabaseMovie(screenplay))
                is TvShow -> tvShowQueries.insertTvShowObject(databaseScreenplayMapper.toDatabaseTvShow(screenplay))
            }
            for (genreSlug in screenplayWithGenreSlugs.genreSlugs) {
                screenplayGenreQueries.insert(
                    genreSlug = genreSlug.toDatabaseId(),
                    screenplayId = screenplayWithGenreSlugs.screenplay.traktId.toDatabaseId()
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
