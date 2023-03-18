package cinescout.utils.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class MoleculeViewModel<Action, State> : ViewModel() {

    private val moleculeScope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    abstract val state: StateFlow<State>

    abstract fun submit(action: Action)

    protected fun launchInScope(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        body: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context, start, body)
    }

    protected fun launchMolecule(
        clock: RecompositionClock = RecompositionClock.ContextClock,
        body: @Composable () -> State
    ): StateFlow<State> = moleculeScope.launchMolecule(clock, body)
}
