package cinescout.suggestions.presentation.viewmodel

import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.suggestions.domain.usecase.GetSuggestionsWithExtras
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.presenter.ForYouPresenter
import cinescout.utils.compose.MoleculeViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Named

@KoinViewModel
internal class ForYouViewModel(
    private val getSuggestionsWithExtras: GetSuggestionsWithExtras,
    private val presenter: ForYouPresenter,
    @Named(SuggestionsStackSizeName) suggestionsStackSize: Int = 10
) : MoleculeViewModel<ForYouAction, ForYouState>() {

    override val state = launchMolecule {
        val suggestedMoviesFlow = getSuggestionsWithExtras(
            type = ScreenplayType.Movies,
            shouldRefreshExtras = false,
            take = suggestionsStackSize
        )

        val suggestedTvShowsFlow = getSuggestionsWithExtras(
            type = ScreenplayType.TvShows,
            shouldRefreshExtras = false,
            take = suggestionsStackSize
        )

        with(presenter) {
            models(
                actionsFlow = actions,
                suggestedMoviesFlow = suggestedMoviesFlow,
                suggestedTvShowsFlow = suggestedTvShowsFlow
            )
        }
    }

    companion object {

        const val SuggestionsStackSizeName = "suggestionsStackSize"
    }
}
