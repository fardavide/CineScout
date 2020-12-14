package client.cli.state

import client.cli.HomeAction
import client.cli.view.SearchView
import client.viewModel.SearchViewModel
import entities.firstRight

class SearchState(val searchViewModel: SearchViewModel) : State() {

    override val actions = setOf(
        HomeAction
    )

    override suspend infix fun execute(command: String): State {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            else -> {
                searchViewModel.search(command)
                SearchResultState(searchViewModel.result.firstRight())
            }
        }
    }

    override fun render() = SearchView(actions).render()
}
