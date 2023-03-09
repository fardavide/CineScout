package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.suggestions.domain.model.SuggestedMovieWithExtras
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.utils.kotlin.combineToLazyList
import cinescout.utils.kotlin.nonEmptyUnsafe
import cinescout.utils.kotlin.shiftWithAnyRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Refresh

interface GetSuggestedMoviesWithExtras {

    operator fun invoke(
        movieExtraRefresh: Refresh = Refresh.IfExpired(),
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedMovieWithExtras>>>
}

@Factory
class RealGetSuggestedMoviesWithExtras(
    private val getSuggestedMovieIds: GetSuggestedMovieIds,
    private val getMovieExtras: GetMovieExtras
) : GetSuggestedMoviesWithExtras {

    override operator fun invoke(
        movieExtraRefresh: Refresh,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedMovieWithExtras>>> =
        getSuggestedMovieIds().flatMapLatest { either ->
            either.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { movies ->
                    movies.take(take).map { suggestedMovieId ->
                        getMovieExtras(
                            suggestedMovieId.movieId,
                            refresh = movieExtraRefresh
                        ).map { movieWithExtrasEither ->
                            movieWithExtrasEither.map { movieWithExtras ->
                                SuggestedMovieWithExtras(
                                    movie = movieWithExtras.movieWithDetails.movie,
                                    affinity = suggestedMovieId.affinity,
                                    genres = movieWithExtras.movieWithDetails.genres,
                                    credits = movieWithExtras.credits,
                                    isInWatchlist = movieWithExtras.isInWatchlist,
                                    keywords = movieWithExtras.keywords,
                                    personalRating = movieWithExtras.personalRating,
                                    source = suggestedMovieId.source
                                )
                            }
                        }
                    }
                        .combineToLazyList()
                        .map { either ->
                            either.shiftWithAnyRight().fold(
                                ifLeft = { dataError -> SuggestionError.Source(dataError as DataError.Remote).left() },
                                ifRight = { it.nonEmptyUnsafe().right() }
                            )
                        }
                }
            )
        }
}

class FakeGetSuggestedMoviesWithExtras(
    private val movies: Nel<SuggestedMovieWithExtras>? = null
) : GetSuggestedMoviesWithExtras {

    override operator fun invoke(
        movieExtraRefresh: Refresh,
        take: Int
    ): Flow<Either<SuggestionError, NonEmptyList<SuggestedMovieWithExtras>>> =
        flowOf(movies?.right() ?: SuggestionError.NoSuggestions.left())
}
