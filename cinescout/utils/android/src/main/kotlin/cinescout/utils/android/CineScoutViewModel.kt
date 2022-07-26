package cinescout.utils.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class CineScoutViewModel<Action, State>(initialState: State) : ViewModel() {

    @PublishedApi
    internal val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()

    abstract fun submit(action: Action)

    protected inline fun updateState(crossinline block: suspend (currentState: State) -> State) {
        viewModelScope.launch {
            mutableState.value = block(mutableState.value)
        }
    }
}
