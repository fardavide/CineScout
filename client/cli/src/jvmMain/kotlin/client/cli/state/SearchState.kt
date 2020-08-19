package client.cli.state

import client.cli.HomeAction
import client.cli.view.SearchView
import client.nextData
import client.viewModel.SearchViewModel

class SearchState(val searchViewModel: SearchViewModel) : State() {

    override val actions = setOf(
        HomeAction
    )

    override suspend infix fun execute(command: String): State {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            else -> {
                searchViewModel.search(command)
                SearchResultState(searchViewModel.result.nextData())
            }
        }
    }

    override fun render() = SearchView(actions).render()
}
