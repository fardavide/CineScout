package client

import domain.SearchMovies
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class SearchViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val searchMovies: SearchMovies,
) : CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Collection<Movie>>()

    private val queryChannel = Channel<String>(99)

    init {
        scope.launch {
            queryChannel.consumeAsFlow()
                .debounce(250)
                .map { searchMovies(it) }
                .catch { result.error = it }
                .collect { result.data = it }
        }
    }

    fun search(query: String) {
        queryChannel.offer(query)
    }

    override fun closeChannels() {
        queryChannel.close()
    }
}
