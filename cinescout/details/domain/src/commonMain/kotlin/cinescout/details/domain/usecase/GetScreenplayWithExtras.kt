@file:Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package cinescout.details.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.continuations.either
import cinescout.details.domain.model.Extra
import cinescout.details.domain.model.ScreenplayWithExtra
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithExtra
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithKeywords
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithScreenplay
import cinescout.details.domain.model.WithWatchlist
import cinescout.error.NetworkError
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.media.domain.usecase.GetScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.usecase.GetScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
import cinescout.rating.domain.usecase.GetPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.store.ScreenplayStore
import cinescout.screenplay.domain.usecase.GetScreenplayGenres
import cinescout.screenplay.domain.usecase.GetScreenplayKeywords
import cinescout.store5.ext.filterData
import cinescout.utils.kotlin.combine
import cinescout.utils.kotlin.exhaustive
import cinescout.watchlist.domain.model.IsInWatchlist
import cinescout.watchlist.domain.usecase.GetIsScreenplayInWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

@Suppress("LongParameterList", "MethodOverloading")
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
@Suppress("MethodOverloading")
internal class RealGetScreenplayWithExtras(
    private val getCredits: GetScreenplayCredits,
    private val getGenres: GetScreenplayGenres,
    private val getIsInWatchlist: GetIsScreenplayInWatchlist,
    private val getKeywords: GetScreenplayKeywords,
    private val getPersonalRating: GetPersonalRating,
    private val getMedia: GetScreenplayMedia,
    private val screenplayStore: ScreenplayStore
) : GetScreenplayWithExtras {

    override fun <S1 : WithExtra, SR> invoke(
        screenplayIds: ScreenplayIds,
        refresh: Boolean,
        refreshExtras: Boolean,
        e1: Extra<S1>
    ): Flow<Either<NetworkError, SR>> where SR : WithScreenplay, SR : WithExtra, SR : S1 {
        val e1Flow = resolve(screenplayIds, e1, refreshExtras)
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
        val e1Flow = resolve(screenplayIds, e1, refreshExtras)
        val e2Flow = resolve(screenplayIds, e2, refreshExtras)
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
        val e1Flow = resolve(screenplayIds, e1, refreshExtras)
        val e2Flow = resolve(screenplayIds, e2, refreshExtras)
        val e3Flow = resolve(screenplayIds, e3, refreshExtras)
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
        val e1Flow = resolve(screenplayIds, e1, refreshExtras)
        val e2Flow = resolve(screenplayIds, e2, refreshExtras)
        val e3Flow = resolve(screenplayIds, e3, refreshExtras)
        val e4Flow = resolve(screenplayIds, e4, refreshExtras)
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
        val e1Flow = resolve(screenplayIds, e1, refreshExtras)
        val e2Flow = resolve(screenplayIds, e2, refreshExtras)
        val e3Flow = resolve(screenplayIds, e3, refreshExtras)
        val e4Flow = resolve(screenplayIds, e4, refreshExtras)
        val e5Flow = resolve(screenplayIds, e5, refreshExtras)
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
        val e1Flow = resolve(screenplayIds, e1, refreshExtras)
        val e2Flow = resolve(screenplayIds, e2, refreshExtras)
        val e3Flow = resolve(screenplayIds, e3, refreshExtras)
        val e4Flow = resolve(screenplayIds, e4, refreshExtras)
        val e5Flow = resolve(screenplayIds, e5, refreshExtras)
        val e6Flow = resolve(screenplayIds, e6, refreshExtras)
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
                is ScreenplayKeywords -> keywords = extra
                is ScreenplayMedia -> media = extra
                else -> throw IllegalArgumentException("Unknown extra: $extra")
            }.exhaustive
        }
    }

    private fun <S : WithExtra> resolve(
        id: ScreenplayIds,
        extra: Extra<S>,
        refresh: Boolean
    ): Flow<Either<NetworkError, Any>> = when (extra) {
        WithCredits -> getCredits(id.tmdb, refresh)
        WithGenres -> getGenres(id.tmdb, refresh)
        WithKeywords -> getKeywords(id.tmdb, refresh)
        WithMedia -> getMedia(id.tmdb, refresh)
        WithPersonalRating -> getPersonalRating(id.tmdb, refresh).wrap()
        WithWatchlist -> getIsInWatchlist(id.tmdb, refresh).wrap()
    }

    @JvmName("wrapPersonalRating")
    private fun Flow<Either<NetworkError, Option<Rating>>>.wrap() =
        map { either -> either.map(::PersonalRating) }

    @JvmName("wrapIsInWatchlist")
    private fun Flow<Either<NetworkError, Boolean>>.wrap() = map { either -> either.map(::IsInWatchlist) }
}
