package screenplay.data.remote.trakt.datasource

import arrow.core.Either
import arrow.core.Nel
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.data.remote.datasource.TraktScreenplayRemoteDataSource
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayWithGenreSlugs
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.utils.kotlin.plus
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper
import screenplay.data.remote.trakt.service.TraktRecommendationService
import screenplay.data.remote.trakt.service.TraktScreenplayService
import screenplay.data.remote.trakt.service.TraktUtilityService

@Factory
class RealTraktScreenplayRemoteDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val recommendationService: TraktRecommendationService,
    private val screenplayMapper: TraktScreenplayMapper,
    private val screenplayService: TraktScreenplayService,
    private val utilityService: TraktUtilityService
) : TraktScreenplayRemoteDataSource {

    override suspend fun getAllGenres(): Either<NetworkError, Nel<Genre>> =
        utilityService.getGenres().map { list ->
            list.map { traktGenre ->
                Genre(
                    name = traktGenre.name,
                    slug = traktGenre.slug
                )
            }
        }

    override suspend fun getRecommended(): Either<NetworkOperation, List<ScreenplayIds>> =
        callWithTraktAccount {
            coroutineScope {
                val moviesDeferred =
                    async { recommendationService.getRecommendedMovies().map { list -> list.map { it.ids.ids } } }
                val tvShowsDeferred =
                    async { recommendationService.getRecommendedTvShows().map { list -> list.map { it.ids.ids } } }
                moviesDeferred.await() + tvShowsDeferred.await()
            }
        }

    override suspend fun getScreenplay(ids: ScreenplayIds): Either<NetworkError, ScreenplayWithGenreSlugs> =
        screenplayService.getScreenplay(ids.trakt).map(screenplayMapper::toScreenplayWithGenreSlugs)

    override suspend fun getSimilar(
        screenplayIds: ScreenplayIds,
        page: Int
    ): Either<NetworkError, List<Screenplay>> =
        screenplayService.getSimilar(screenplayIds.trakt, page).map { list ->
            list.map { screenplayMapper.toScreenplayWithGenreSlugs(it).screenplay }
        }
}
