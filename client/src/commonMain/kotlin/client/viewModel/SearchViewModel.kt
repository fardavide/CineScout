package client.viewModel

import domain.SearchMovies
import entities.Either
import entities.left
import entities.movies.Movie
import entities.movies.SearchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.milliseconds

class SearchViewModel(
    override val scope: CoroutineScope,
    private val searchMovies: SearchMovies,
) : CineViewModel {

    private val _result: MutableStateFlow<Either<SearchError, Collection<Movie>>> =
        MutableStateFlow(SearchError.EmptyQuery.left())

    val result: StateFlow<Either<SearchError, Collection<Movie>>> =
        _result.asStateFlow()

    private val queryChannel = Channel<String>(99)

    init {
        scope.launch {
            queryChannel.consumeAsFlow()
                .debounce(QueryDebounceInterval)
                .collect {
                    _result.value = searchMovies(it)
                }
        }
    }

    infix fun search(query: String) {
        queryChannel.offer(query)
    }

    override fun closeChannels() {
        queryChannel.close()
    }

    companion object {
        val QueryDebounceInterval = 250.milliseconds
    }
}
