package client.viewModel

import client.ViewState.Loading
import client.ViewState.None
import client.ViewState.Success
import client.ViewStateFlow
import domain.SearchMovies
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    override val scope: CoroutineScope,
    private val searchMovies: SearchMovies,
) : CineViewModel {

    val result = ViewStateFlow<Collection<Movie>>()

    private val queryChannel = Channel<String>(99)

    init {
        scope.launch {
            queryChannel.consumeAsFlow()
                .onStart { result.state = Loading }
                .debounce(250)
                .map { query ->
                    if (query.length >= 2) Success(searchMovies(query))
                    else None
                }
                .broadcastFoldingIn(result)
        }
    }

    infix fun search(query: String) {
        queryChannel.offer(query)
    }

    override fun closeChannels() {
        queryChannel.close()
    }
}
