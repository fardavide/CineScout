package store

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import store.builder.toPagedData

/**
 * Creates a flow that combines Local data and Remote data, when remote data is paged
 * @see PagedStore
 *
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors that wrap
 *  a Local data
 *
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
fun <T, P : Paging> PagedStore(
    fetch: suspend (page: Int) -> Either<NetworkError, PagedData.Remote<T, Paging.Page>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit
): PagedStore<T, P> = PagedStore(
    initialBookmark = 1,
    createNextBookmark = { _, currentBookmark: Int -> currentBookmark + 1 },
    fetch = fetch,
    read = read,
    write = write
)

/**
 * Creates a flow that combines Local data and Remote data, when remote data is paged
 * @see PagedStore
 *
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors that wrap
 *  a Local data
 *
 * @param initialBookmark initial bookmark to start fetching from
 * @param createNextBookmark lambda that creates the next bookmark to fetch from
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
@BuilderInference
fun <T, B, PI : Paging.Page, PO : Paging> PagedStore(
    initialBookmark: B,
    createNextBookmark: (lastData: PagedData<T, PI>, currentBookmark: B) -> B,
    fetch: suspend (bookmark: B) -> Either<NetworkError, PagedData.Remote<T, PI>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit
): PagedStore<T, PO> {
    var bookmark = initialBookmark
    val loadMoreTrigger = MutableStateFlow(initialBookmark)
    val onLoadMore = { loadMoreTrigger.value = bookmark }
    var shouldLoadAll = false

    val flow = buildPagedStoreFlow<T, B, PI, PO>(
        fetch = { fetch(bookmark).tap { data -> bookmark = createNextBookmark(data, bookmark) } },
        read = read,
        write = write,
        loadMoreTrigger = loadMoreTrigger
    ).onEach { either ->
        either.tap { data ->
            if (shouldLoadAll && data.isLastPage().not()) {
                onLoadMore()
            }
        }
    }
    return PagedStoreImpl(
        flow = flow,
        onLoadMore = onLoadMore,
        onLoadAll = {
            shouldLoadAll = true
            onLoadMore()
        }
    )
}

interface PagedStore<T, P : Paging> : Store<PagedData<T, P>> {

    fun filterIntermediatePages(): Flow<Either<DataError, PagedData<T, P>>> =
        loadAll()
            .filter { either ->
                either.fold(
                    ifLeft = { false },
                    ifRight = { pagedData -> pagedData.isLastPage() }
                )
            }

    suspend fun getAll(): Either<DataError, List<T>>

    fun loadAll(): PagedStore<T, P>

    fun loadMore(): PagedStore<T, P>
}

internal class PagedStoreImpl<T, P : Paging>(
    internal val flow: Flow<Either<DataError, PagedData<T, P>>>,
    internal val onLoadMore: () -> Unit,
    internal val onLoadAll: () -> Unit
) : PagedStore<T, P>, Flow<Either<DataError, PagedData<T, P>>> by flow {

    override suspend fun getAll(): Either<DataError, List<T>> {
        onLoadAll()
        return this.transform { either ->
            either.tap { pagedData ->
                if (pagedData.isLastPage()) {
                    emit(pagedData.data.right())
                }
            }.tapLeft { error ->
                emit(error.left())
            }
        }.first()
    }

    override fun loadAll(): PagedStore<T, P> {
        onLoadAll()
        return this
    }

    override fun loadMore(): PagedStore<T, P> {
        onLoadMore()
        return this
    }
}

private fun <T, B, PI : Paging.Page, PO : Paging> buildPagedStoreFlow(
    fetch: suspend (bookmark: B) -> Either<NetworkError, PagedData.Remote<T, PI>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit,
    loadMoreTrigger: Flow<B>
): Flow<Either<DataError.Remote, PagedData<T, PO>>> =
    combineTransform(
        loadMoreTrigger.transform<B, ConsumableData<PagedData.Remote<T, PI>>?> { bookmark ->
            val remoteDataEither = fetch(bookmark)
            remoteDataEither.tap { remoteData ->
                write(remoteData.data)
            }
            emit(ConsumableData.of(remoteDataEither))
        }.onStart { emit(null) },
        read().map { either -> either.map { list -> list.toPagedData() } }
    ) { consumableRemoteData, localEither ->

        val remoteEither = consumableRemoteData?.consume()
        if (remoteEither != null) {
            val result = either {
                val remoteData = remoteEither
                    .mapLeft(DataError::Remote)
                    .bind()

                localEither.fold(
                    ifLeft = {
                        if (remoteData.isFirstPage()) remoteData
                        else throw AssertionError("Remote data is not first page, but there is no cached data")
                    },
                    ifRight = { localData -> remoteData.copy(data = localData.data) }
                )
            }
            emit(result as Either<DataError.Remote, PagedData<T, PO>>)
        } else {
            localEither.tap { local -> emit(local.right() as Either<DataError.Remote, PagedData<T, PO>>) }
        }
    }