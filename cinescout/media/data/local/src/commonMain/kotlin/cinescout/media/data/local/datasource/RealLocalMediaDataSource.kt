package cinescout.media.data.local.datasource

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.database.ScreenplayBackdropQueries
import cinescout.database.ScreenplayPosterQueries
import cinescout.database.ScreenplayVideoQueries
import cinescout.database.util.suspendTransaction
import cinescout.media.data.datasource.LocalMediaDataSource
import cinescout.media.data.local.mapper.DatabaseImageMapper
import cinescout.media.data.local.mapper.DatabaseVideoMapper
import cinescout.media.domain.model.ScreenplayImages
import cinescout.media.domain.model.ScreenplayVideos
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalMediaDataSource(
    private val imageMapper: DatabaseImageMapper,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val screenplayBackdropQueries: ScreenplayBackdropQueries,
    private val screenplayPosterQueries: ScreenplayPosterQueries,
    private val screenplayVideoQueries: ScreenplayVideoQueries,
    private val transacter: Transacter,
    private val videoMapper: DatabaseVideoMapper,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalMediaDataSource {

    override fun findImages(screenplayId: TmdbScreenplayId): Flow<ScreenplayImages?> = combine(
        screenplayBackdropQueries
            .findAllByScreenplayId(screenplayId.toDatabaseId(), imageMapper::toBackdrop)
            .asFlow()
            .mapToList(readDispatcher),
        screenplayPosterQueries
            .findAllByScreenplayId(screenplayId.toDatabaseId(), imageMapper::toPoster)
            .asFlow()
            .mapToList(readDispatcher)
    ) { backdrops, posters ->
        ScreenplayImages(
            screenplayId = screenplayId,
            backdrops = backdrops,
            posters = posters
        ).takeIf { backdrops.isNotEmpty() || posters.isNotEmpty() }
    }

    override fun findVideos(screenplayId: TmdbScreenplayId): Flow<ScreenplayVideos> =
        screenplayVideoQueries.findAllByScreenplayId(screenplayId.toDatabaseId(), videoMapper::toVideo)
            .asFlow()
            .mapToList(readDispatcher)
            .map { videos ->
                ScreenplayVideos(
                    screenplayId = screenplayId,
                    videos = videos
                )
            }

    override suspend fun insertImages(images: ScreenplayImages) {
        transacter.suspendTransaction(writeDispatcher) {
            val databaseBackdrops = imageMapper.toDatabaseBackdrops(images)
            for (databaseBackdrop in databaseBackdrops) {
                screenplayBackdropQueries.insert(databaseBackdrop)
            }
            val databasePosters = imageMapper.toDatabasePosters(images)
            for (databasePoster in databasePosters) {
                screenplayPosterQueries.insert(databasePoster)
            }
        }
    }

    override suspend fun insertVideos(videos: ScreenplayVideos) {
        screenplayVideoQueries.suspendTransaction(writeDispatcher) {
            val databaseVideos = videoMapper.toDatabaseVideos(videos)
            for (databaseVideo in databaseVideos) {
                screenplayVideoQueries.insert(databaseVideo)
            }
        }
    }
}
