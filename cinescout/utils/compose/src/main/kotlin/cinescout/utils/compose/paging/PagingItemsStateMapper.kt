package cinescout.utils.compose.paging

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import cinescout.resources.TextRes
import cinescout.store5.FetchException
import cinescout.unsupported
import cinescout.utils.android.NetworkErrorToMessageMapper
import cinescout.utils.compose.Effect
import org.koin.core.annotation.Factory

interface PagingItemsStateMapper {

    fun <T : Any> toState(items: LazyPagingItems<T>): PagingItemsState<T>
}

@Factory
internal class RealPagingItemsStateMapper(
    private val messageMapper: NetworkErrorToMessageMapper,
    private val surrogateMapper: LazyPagingItemsSurrogateMapper
) : PagingItemsStateMapper {

    override fun <T : Any> toState(items: LazyPagingItems<T>): PagingItemsState<T> =
        toState(items, surrogateMapper.toSurrogate(items))

    private fun <T : Any> toState(
        items: LazyPagingItems<T>,
        surrogate: LazyPagingItemsSurrogate
    ): PagingItemsState<T> = when (surrogate.itemCount) {
        0 -> when {
            surrogate.loadState.refresh is LoadState.Loading ->
                PagingItemsState.Loading

            surrogate.loadState.refresh is LoadState.NotLoading ->
                PagingItemsState.Empty

            surrogate.loadState.append is LoadState.Error ->
                PagingItemsState.Error(toErrorMessage(surrogate.loadState.append as LoadState.Error))

            surrogate.loadState.refresh is LoadState.Error ->
                PagingItemsState.Error(toErrorMessage(surrogate.loadState.refresh as LoadState.Error))

            surrogate.loadState.prepend is LoadState.Error ->
                PagingItemsState.Error(toErrorMessage(surrogate.loadState.prepend as LoadState.Error))

            else -> unsupported
        }
        else -> PagingItemsState.NotEmpty(
            items = items,
            error = toErrorMessage(surrogate),
            isAlsoLoading = surrogate.loadState.refresh is LoadState.Loading
        )
    }

    private fun toErrorMessage(loadState: LoadState.Error): TextRes = when (val error = loadState.error) {
        is FetchException -> messageMapper.toMessage(error.networkError)
        else -> error("Unknown error: $error")
    }

    private fun toErrorMessage(surrogate: LazyPagingItemsSurrogate): Effect<TextRes> =
        when (surrogate.itemCount) {
            0 -> Effect.empty()
            else -> when {
                surrogate.loadState.prepend is LoadState.Error ->
                    Effect.of(toErrorMessage(surrogate.loadState.prepend as LoadState.Error))

                surrogate.loadState.refresh is LoadState.Error ->
                    Effect.of(toErrorMessage(surrogate.loadState.refresh as LoadState.Error))

                surrogate.loadState.append is LoadState.Error ->
                    Effect.of(toErrorMessage(surrogate.loadState.append as LoadState.Error))

                else -> Effect.empty()
            }
        }
}

class FakePagingItemsStateMapper : PagingItemsStateMapper {

    override fun <T : Any> toState(items: LazyPagingItems<T>): PagingItemsState<T> = PagingItemsState.Loading
}

internal interface LazyPagingItemsSurrogateMapper {

    fun <T : Any> toSurrogate(items: LazyPagingItems<T>) = LazyPagingItemsSurrogate(
        itemCount = items.itemCount,
        loadState = items.loadState
    )
}

@Factory
internal class RealLazyPagingItemsSurrogateMapper : LazyPagingItemsSurrogateMapper

internal class FakeLazyPagingItemsSurrogateMapper(
    private val surrogate: LazyPagingItemsSurrogate
) : LazyPagingItemsSurrogateMapper {

    override fun <T : Any> toSurrogate(items: LazyPagingItems<T>) = surrogate
}
