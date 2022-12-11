package cinescout.suggestions.domain.usecase

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.right
import cinescout.common.model.SuggestionError
import cinescout.error.DataError
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.usecase.GetMovieExtras
import cinescout.utils.kotlin.combineToLazyList
import cinescout.utils.kotlin.nonEmptyUnsafe
import cinescout.utils.kotlin.shiftWithAnyRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import store.Refresh

@Factory
class GetSuggestedMoviesWithExtras(
    private val getSuggestedMovies: GetSuggestedMovies,
    private val getMovieExtras: GetMovieExtras
) {

    operator fun invoke(
        movieExtraRefresh: Refresh = Refresh.IfExpired(),
        take: Int = Integer.MAX_VALUE
    ): Flow<Either<SuggestionError, NonEmptyList<MovieWithExtras>>> =
        getSuggestedMovies().flatMapLatest { either ->
            either.fold(
                ifLeft = { suggestionError -> flowOf(suggestionError.left()) },
                ifRight = { movies ->
                    movies.take(take).map { movie -> getMovieExtras(movie, refresh = movieExtraRefresh) }
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
