package client.cli.state

import client.cli.HomeAction
import client.cli.view.RateMovie
import client.viewModel.RateMovieViewModel
import entities.Rating
import entities.TmdbId

class RateMovieState(
    private val rateMovieViewModel: RateMovieViewModel
) : State() {

    override val actions = setOf(
        HomeAction
    )

    override suspend infix fun execute(command: String): State {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            else -> {
                rateMovieViewModel[TmdbId(command.toInt())] = Rating.Positive
                RateMovieState(rateMovieViewModel)
            }
        }
    }

    override fun render() = RateMovie(actions).render()
}
