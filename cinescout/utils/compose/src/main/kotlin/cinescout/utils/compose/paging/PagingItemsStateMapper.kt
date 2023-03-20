package cinescout.utils.compose.paging

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import cinescout.resources.TextRes
import cinescout.store5.FetchException
import cinescout.unsupported
import cinescout.utils.compose.Effect
import cinescout.utils.compose.NetworkErrorToMessageMapper
import org.koin.core.annotation.Factory

@Factory
class PagingItemsStateMapper(
    private val messageMapper: NetworkErrorToMessageMapper
) {

    fun <T : Any> toState(items: LazyPagingItems<T>): PagingItemsState = when {
        items.itemCount == 0 -> when {

            items.loadState.refresh is LoadState.Loading ->
                PagingItemsState.Loading

            items.loadState.refresh is LoadState.NotLoading ->
                PagingItemsState.Empty

            items.loadState.append is LoadState.Error ->
                PagingItemsState.Error(toErrorMessage(items.loadState.append as LoadState.Error))

            items.loadState.refresh is LoadState.Error ->
                PagingItemsState.Error(toErrorMessage(items.loadState.refresh as LoadState.Error))

            items.loadState.prepend is LoadState.Error ->
                PagingItemsState.Error(toErrorMessage(items.loadState.prepend as LoadState.Error))

            else -> unsupported
        }
        else -> PagingItemsState.NotEmpty(items, toErrorMessage(items))
    }

    private fun toErrorMessage(loadState: LoadState.Error): TextRes = when (val error = loadState.error) {
        is FetchException -> messageMapper.toMessage(error.networkError)
        else -> error("Unknown error: $error")
    }

    private fun <T : Any> toErrorMessage(items: LazyPagingItems<T>): Effect<TextRes> =
        when (items.itemCount) {
            0 -> Effect.empty()
            else -> when {

                items.loadState.append is LoadState.Error ->
                    Effect.of(toErrorMessage(items.loadState.prepend as LoadState.Error))

                items.loadState.refresh is LoadState.Error ->
                    Effect.of(toErrorMessage(items.loadState.refresh as LoadState.Error))

                items.loadState.prepend is LoadState.Error ->
                    Effect.of(toErrorMessage(items.loadState.prepend as LoadState.Error))

                else -> Effect.empty()
            }
        }
}
