package cinescout.suggestions.presentation.viewmodel

import androidx.compose.runtime.Composable
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.suggestions.domain.usecase.GetSuggestionsWithExtras
import cinescout.suggestions.presentation.action.ForYouAction
import cinescout.suggestions.presentation.presenter.ForYouPresenter
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.utils.compose.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Named

@KoinViewModel
internal class ForYouViewModel(
    private val getSuggestionsWithExtras: GetSuggestionsWithExtras,
    private val presenter: ForYouPresenter,
    @Named(SuggestionsStackSizeName) private val suggestionsStackSize: Int = 10
) : MoleculeViewModel<ForYouAction, ForYouState>() {

    @Composable
    override fun models(actions: Flow<ForYouAction>): ForYouState {
        val suggestedMoviesFlow = getSuggestionsWithExtras(
            type = ScreenplayTypeFilter.Movies,
            shouldRefreshExtras = false,
            take = suggestionsStackSize
        )

        val suggestedTvShowsFlow = getSuggestionsWithExtras(
            type = ScreenplayTypeFilter.TvShows,
            shouldRefreshExtras = false,
            take = suggestionsStackSize
        )

        return presenter.models(
            actionsFlow = actions,
            suggestedMoviesFlow = suggestedMoviesFlow,
            suggestedTvShowsFlow = suggestedTvShowsFlow
        )
    }

    companion object {

        const val SuggestionsStackSizeName = "suggestionsStackSize"
    }
}
