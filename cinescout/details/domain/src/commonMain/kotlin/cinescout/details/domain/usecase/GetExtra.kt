package cinescout.details.domain.usecase

import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import arrow.core.right
import cinescout.details.domain.model.Extra
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithExtra
import cinescout.details.domain.model.WithGenres
import cinescout.details.domain.model.WithKeywords
import cinescout.details.domain.model.WithMedia
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithWatchlist
import cinescout.error.NetworkError
import cinescout.media.domain.model.ScreenplayMedia
import cinescout.media.domain.usecase.GetScreenplayMedia
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.usecase.GetScreenplayCredits
import cinescout.rating.domain.model.PersonalRating
import cinescout.rating.domain.usecase.GetPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayGenres
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.ScreenplayKeywords
import cinescout.screenplay.domain.usecase.GetScreenplayGenres
import cinescout.screenplay.domain.usecase.GetScreenplayKeywords
import cinescout.watchlist.domain.model.IsInWatchlist
import cinescout.watchlist.domain.usecase.GetIsScreenplayInWatchlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.annotation.Factory

interface GetExtra {

    operator fun <S : WithExtra> invoke(
        id: ScreenplayIds,
        extra: Extra<S>,
        refresh: Boolean
    ): Flow<Either<NetworkError, Any>>
}

@Factory
internal class RealGetExtra(
    private val getCredits: GetScreenplayCredits,
    private val getGenres: GetScreenplayGenres,
    private val getIsInWatchlist: GetIsScreenplayInWatchlist,
    private val getKeywords: GetScreenplayKeywords,
    private val getPersonalRating: GetPersonalRating,
    private val getMedia: GetScreenplayMedia
) : GetExtra {

    override operator fun <S : WithExtra> invoke(
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

@VisibleForTesting
class FakeGetExtra(
    private val credits: ScreenplayCredits? = null,
    private val genres: ScreenplayGenres? = null,
    isInWatchlist: IsInWatchlist? = null,
    isInWatchlistFlow: Flow<IsInWatchlist>? = isInWatchlist?.let(::flowOf),
    private val isInWatchlistEitherFlow: Flow<Either<NetworkError, IsInWatchlist>> =
        isInWatchlistFlow?.map { it.right() } ?: flowOf(NetworkError.Unknown.left()),
    private val keywords: ScreenplayKeywords? = null,
    private val media: ScreenplayMedia? = null,
    personalRating: PersonalRating? = null,
    personalRatingFlow: Flow<PersonalRating>? = personalRating?.let(::flowOf),
    private val personalRatingEitherFlow: Flow<Either<NetworkError, PersonalRating>> =
        personalRatingFlow?.map { it.right() } ?: flowOf(NetworkError.Unknown.left())
) : GetExtra {

    override fun <S : WithExtra> invoke(
        id: ScreenplayIds,
        extra: Extra<S>,
        refresh: Boolean
    ): Flow<Either<NetworkError, Any>> =
        combine(isInWatchlistEitherFlow, personalRatingEitherFlow) { isInWatchlist, personalRating ->
            when (extra) {
                WithCredits -> credits?.right() ?: NetworkError.Unknown.left()
                WithGenres -> genres?.right() ?: NetworkError.Unknown.left()
                WithKeywords -> keywords?.right() ?: NetworkError.Unknown.left()
                WithMedia -> media?.right() ?: NetworkError.Unknown.left()
                WithPersonalRating -> personalRating
                WithWatchlist -> isInWatchlist
            }
        }
}
