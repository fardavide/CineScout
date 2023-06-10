@file:Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package cinescout.details.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import cinescout.CineScoutTestApi
import cinescout.details.domain.model.Extra
import cinescout.details.domain.model.ScreenplayWithExtra
import cinescout.details.domain.model.WithExtra
import cinescout.details.domain.model.WithScreenplay
import cinescout.error.NetworkError
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.store5.ext.filterData
import cinescout.utils.kotlin.combine
import cinescout.utils.kotlin.exhaustive
import cinescout.watchlist.domain.model.IsInWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetScreenplayWithExtras {

    operator fun <S1 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1

    operator fun <S1 : WithExtra, S2 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2

    operator fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2, SR : S3

    operator fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2, SR : S3, SR : S4

    operator fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, S5 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        e5: Extra<S5>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4,
          SR : S5

    operator fun <
        S1 : WithExtra,
        S2 : WithExtra,
        S3 : WithExtra,
        S4 : WithExtra,
        S5 : WithExtra,
        S6 : WithExtra,
        SR
        >
    invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        e5: Extra<S5>,
        e6: Extra<S6>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4,
          SR : S5,
          SR : S6
}

@Factory
internal class RealGetScreenplayWithExtras(
    private val getExtra: GetExtra,
    private val screenplayStore: ScreenplayStore
) : GetScreenplayWithExtras {

    override fun <S1 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1 {
        val e1Flow = getExtra(screenplayIds, e1, refreshExtras)
        return combine(
            getScreenplay(screenplayIds, refresh),
            e1Flow
        ) { screenplayEither, e1Either ->
            either {
                val screenplay = screenplayEither.bind()
                val extra1 = e1Either.bind()
                @Suppress("UNCHECKED_CAST")
                ScreenplayWithExtra(screenplay).applyExtras(extra1) as SR
            }
        }
    }

    override fun <S1 : WithExtra, S2 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2 {
        val e1Flow = getExtra(screenplayIds, e1, refreshExtras)
        val e2Flow = getExtra(screenplayIds, e2, refreshExtras)
        return combine(
            getScreenplay(screenplayIds, refresh),
            e1Flow,
            e2Flow
        ) { screenplayEither, e1Either, e2Either ->
            either {
                val screenplay = screenplayEither.bind()
                val extra1 = e1Either.bind()
                val extra2 = e2Either.bind()
                @Suppress("UNCHECKED_CAST")
                ScreenplayWithExtra(screenplay).applyExtras(extra1, extra2) as SR
            }
        }
    }

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2, SR : S3 {
        val e1Flow = getExtra(screenplayIds, e1, refreshExtras)
        val e2Flow = getExtra(screenplayIds, e2, refreshExtras)
        val e3Flow = getExtra(screenplayIds, e3, refreshExtras)
        return combine(
            getScreenplay(screenplayIds, refresh),
            e1Flow,
            e2Flow,
            e3Flow
        ) { screenplayEither, e1Either, e2Either, e3Either ->
            either {
                val screenplay = screenplayEither.bind()
                val extra1 = e1Either.bind()
                val extra2 = e2Either.bind()
                val extra3 = e3Either.bind()
                @Suppress("UNCHECKED_CAST")
                ScreenplayWithExtra(screenplay).applyExtras(extra1, extra2, extra3) as SR
            }
        }
    }

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2, SR : S3, SR : S4 {
        val e1Flow = getExtra(screenplayIds, e1, refreshExtras)
        val e2Flow = getExtra(screenplayIds, e2, refreshExtras)
        val e3Flow = getExtra(screenplayIds, e3, refreshExtras)
        val e4Flow = getExtra(screenplayIds, e4, refreshExtras)
        return combine(
            getScreenplay(screenplayIds, refresh),
            e1Flow,
            e2Flow,
            e3Flow,
            e4Flow
        ) { screenplayEither, e1Either, e2Either, e3Either, e4Either ->
            either {
                val screenplay = screenplayEither.bind()
                val extra1 = e1Either.bind()
                val extra2 = e2Either.bind()
                val extra3 = e3Either.bind()
                val extra4 = e4Either.bind()
                @Suppress("UNCHECKED_CAST")
                ScreenplayWithExtra(screenplay).applyExtras(extra1, extra2, extra3, extra4) as SR
            }
        }
    }

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, S5 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        e5: Extra<S5>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4,
          SR : S5 {
        val e1Flow = getExtra(screenplayIds, e1, refreshExtras)
        val e2Flow = getExtra(screenplayIds, e2, refreshExtras)
        val e3Flow = getExtra(screenplayIds, e3, refreshExtras)
        val e4Flow = getExtra(screenplayIds, e4, refreshExtras)
        val e5Flow = getExtra(screenplayIds, e5, refreshExtras)
        return combine(
            getScreenplay(screenplayIds, refresh),
            e1Flow,
            e2Flow,
            e3Flow,
            e4Flow,
            e5Flow
        ) { screenplayEither, e1Either, e2Either, e3Either, e4Either, e5Either ->
            either {
                val screenplay = screenplayEither.bind()
                val extra1 = e1Either.bind()
                val extra2 = e2Either.bind()
                val extra3 = e3Either.bind()
                val extra4 = e4Either.bind()
                val extra5 = e5Either.bind()
                @Suppress("UNCHECKED_CAST")
                ScreenplayWithExtra(screenplay).applyExtras(extra1, extra2, extra3, extra4, extra5) as SR
            }
        }
    }

    override fun <
        S1 : WithExtra,
        S2 : WithExtra,
        S3 : WithExtra,
        S4 : WithExtra,
        S5 : WithExtra,
        S6 : WithExtra,
        SR
        > invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        e5: Extra<S5>,
        e6: Extra<S6>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4,
          SR : S5,
          SR : S6 {
        val e1Flow = getExtra(screenplayIds, e1, refreshExtras)
        val e2Flow = getExtra(screenplayIds, e2, refreshExtras)
        val e3Flow = getExtra(screenplayIds, e3, refreshExtras)
        val e4Flow = getExtra(screenplayIds, e4, refreshExtras)
        val e5Flow = getExtra(screenplayIds, e5, refreshExtras)
        val e6Flow = getExtra(screenplayIds, e6, refreshExtras)
        return combine(
            getScreenplay(screenplayIds, refresh),
            e1Flow,
            e2Flow,
            e3Flow,
            e4Flow,
            e5Flow,
            e6Flow
        ) { screenplayEither, e1Either, e2Either, e3Either, e4Either, e5Either, e6Either ->
            either {
                val screenplay = screenplayEither.bind()
                val extra1 = e1Either.bind()
                val extra2 = e2Either.bind()
                val extra3 = e3Either.bind()
                val extra4 = e4Either.bind()
                val extra5 = e5Either.bind()
                val extra6 = e6Either.bind()
                @Suppress("UNCHECKED_CAST")
                ScreenplayWithExtra(screenplay).applyExtras(extra1, extra2, extra3, extra4, extra5, extra6) as SR
            }
        }
    }

    private fun getScreenplay(
        screenplayIds: ScreenplayIds,
        refresh: Boolean
    ): Flow<Either<NetworkError, Screenplay>> = screenplayStore
        .stream(StoreReadRequest.cached(screenplayIds, refresh))
        .filterData()

    private fun ScreenplayWithExtra.applyExtras(vararg extras: Any): ScreenplayWithExtra = apply {
        for (extra in extras) {
            when (extra) {
                is IsInWatchlist -> isInWatchlistBoxed = extra
                is PersonalRating -> personalRatingBoxed = extra
                is ScreenplayCredits -> credits = extra
                is ScreenplayGenres -> genres = extra
                is ScreenplayHistory -> history = extra
                is ScreenplayKeywords -> keywords = extra
                is ScreenplayMedia -> media = extra
                else -> throw IllegalArgumentException("Unknown extra: $extra")
            }.exhaustive
        }
    }
}

@CineScoutTestApi
@Suppress("UNCHECKED_CAST")
class FakeGetScreenplayWithExtras(
    private val screenplayWithExtras: ScreenplayWithExtra? = null,
    private val screenplayWithExtraFlow: Flow<ScreenplayWithExtra>? = screenplayWithExtras?.let(::flowOf),
    private val screenplayWithExtraEitherFlow: Flow<Either<NetworkError, ScreenplayWithExtra>> =
        screenplayWithExtraFlow?.map { it.right() } ?: flowOf(NetworkError.Unknown.left())
) : GetScreenplayWithExtras {

    override fun <S1 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1 = flow()

    override fun <S1 : WithExtra, S2 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2 = flow()

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2, SR : S3 =
        flow()

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1, SR : S2, SR : S3, SR : S4 =
        flow()

    override fun <S1 : WithExtra, S2 : WithExtra, S3 : WithExtra, S4 : WithExtra, S5 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        e5: Extra<S5>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4,
          SR : S5 =
        flow()

    override fun <
        S1 : WithExtra,
        S2 : WithExtra,
        S3 : WithExtra,
        S4 : WithExtra,
        S5 : WithExtra,
        S6 : WithExtra,
        SR
        > invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>,
        e2: Extra<S2>,
        e3: Extra<S3>,
        e4: Extra<S4>,
        e5: Extra<S5>,
        e6: Extra<S6>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay,
          SR : WithExtra,
          SR : S1,
          SR : S2,
          SR : S3,
          SR : S4,
          SR : S5,
          SR : S6 =
        flow()

    fun <SR> flow() = screenplayWithExtraEitherFlow as Flow<Either<NetworkError, SR>>
}
