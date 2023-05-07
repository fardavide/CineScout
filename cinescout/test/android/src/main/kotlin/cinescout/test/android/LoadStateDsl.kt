package cinescout.test.android

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import kotlin.reflect.KProperty

fun createCombinedLoadStates(
    refresh: LoadState = IdleLoadState(),
    prepend: LoadState = IdleLoadState(),
    append: LoadState = IdleLoadState(),
    source: LoadStates = createLoadStates(),
    mediator: LoadStates = createLoadStates()
) = CombinedLoadStates(
    refresh = refresh,
    prepend = prepend,
    append = append,
    source = source,
    mediator = mediator
)

fun createLoadStates(
    refresh: LoadState = IdleLoadState(),
    prepend: LoadState = IdleLoadState(),
    append: LoadState = IdleLoadState()
) = LoadStates(
    refresh = refresh,
    prepend = prepend,
    append = append
)

fun IdleLoadState() = LoadState.NotLoading(endOfPaginationReached = false)

@CombinedLoadStatesBuilderDsl
fun buildCombinedLoadStates(build: CombinedLoadStatesBuilder.() -> Unit) =
    CombinedLoadStatesBuilder().apply(build).build()

@LoadStatesBuilderDsl
fun buildLoadStates(build: LoadStatesBuilder.() -> Unit): LoadStates =
    LoadStatesBuilder(combinedLoadStatesBuilder = null, setParent = {}).apply(build).build()

@LoadStateBuilderDsl
fun buildLoadState(build: LoadStateBuilder.() -> Unit): LoadState =
    LoadStateBuilder(combinedLoadStatesBuilder = null, setParent = {}).apply(build).build()

val CombinedLoadStates get() = CombinedLoadStatesBuilder()

operator fun CombinedLoadStates?.getValue(nothing: Nothing?, property: KProperty<*>): CombinedLoadStates =
    checkNotNull(this)

@CombinedLoadStatesBuilderDsl
class CombinedLoadStatesBuilder internal constructor() {
    private var _refresh: LoadState = IdleLoadState()
    private var _prepend: LoadState = IdleLoadState()
    private var _append: LoadState = IdleLoadState()
    private var _source: LoadStates = createLoadStates()
    private var _mediator: LoadStates = createLoadStates()

    val refresh = LoadStateBuilder(combinedLoadStatesBuilder = this) { _refresh = it }
    val prepend = LoadStateBuilder(combinedLoadStatesBuilder = this) { _prepend = it }
    val append = LoadStateBuilder(combinedLoadStatesBuilder = this) { _append = it }
    val source = LoadStatesBuilder(combinedLoadStatesBuilder = this) { _source = it }
    val mediator = LoadStatesBuilder(combinedLoadStatesBuilder = this) { _mediator = it }

    fun refresh(@LoadStateBuilderDsl build: LoadStateBuilder.() -> Unit) =
        apply { _refresh = buildLoadState(build) }
    fun prepend(@LoadStateBuilderDsl build: LoadStateBuilder.() -> Unit) =
        apply { _prepend = buildLoadState(build) }
    fun append(@LoadStateBuilderDsl build: LoadStateBuilder.() -> Unit) =
        apply { _append = buildLoadState(build) }
    fun source(@LoadStatesBuilderDsl build: LoadStatesBuilder.() -> Unit) =
        apply { _source = buildLoadStates(build) }
    fun mediator(@LoadStatesBuilderDsl build: LoadStatesBuilder.() -> Unit) =
        apply { _mediator = buildLoadStates(build) }

    internal fun build() = createCombinedLoadStates(
        refresh = _refresh,
        prepend = _prepend,
        append = _append,
        source = _source,
        mediator = _mediator
    )
}

@LoadStatesBuilderDsl
class LoadStatesBuilder internal constructor(
    private val combinedLoadStatesBuilder: CombinedLoadStatesBuilder?,
    private val setParent: (LoadStates) -> Unit
) {
    private var _refresh: LoadState = IdleLoadState()
    private var _prepend: LoadState = IdleLoadState()
    private var _append: LoadState = IdleLoadState()

    val refresh = LoadStateBuilder(combinedLoadStatesBuilder) { _refresh = it; setParent(build()) }
    val prepend = LoadStateBuilder(combinedLoadStatesBuilder) { _prepend = it; setParent(build()) }
    val append = LoadStateBuilder(combinedLoadStatesBuilder) { _append = it; setParent(build()) }

    fun refresh(@LoadStateBuilderDsl build: LoadStateBuilder.() -> Unit) =
        apply { _refresh = buildLoadState(build) }
    fun prepend(@LoadStateBuilderDsl build: LoadStateBuilder.() -> Unit) =
        apply { _prepend = buildLoadState(build) }
    fun append(@LoadStateBuilderDsl build: LoadStateBuilder.() -> Unit) =
        apply { _append = buildLoadState(build) }

    internal fun build() = createLoadStates(
        refresh = _refresh,
        prepend = _prepend,
        append = _append
    )
}

@LoadStateBuilderDsl
class LoadStateBuilder internal constructor(
    private val combinedLoadStatesBuilder: CombinedLoadStatesBuilder?,
    private val setParent: (LoadState) -> Unit
) {
    private var _value: LoadState = IdleLoadState()

    fun complete(): CombinedLoadStates? {
        _value = LoadState.NotLoading(endOfPaginationReached = true).also(setParent)
        return combinedLoadStatesBuilder?.build()
    }
    fun error(message: String): CombinedLoadStates? {
        _value = LoadState.Error(Exception(message)).also(setParent)
        return combinedLoadStatesBuilder?.build()
    }
    fun error(throwable: Throwable): CombinedLoadStates? {
        _value = LoadState.Error(throwable).also(setParent)
        return combinedLoadStatesBuilder?.build()
    }
    fun incomplete(): CombinedLoadStates? {
        _value = LoadState.NotLoading(endOfPaginationReached = false).also(setParent)
        return combinedLoadStatesBuilder?.build()
    }
    fun loading(): CombinedLoadStates? {
        _value = LoadState.Loading.also(setParent)
        return combinedLoadStatesBuilder?.build()
    }

    internal fun build() = _value
}

@DslMarker
annotation class CombinedLoadStatesBuilderDsl

@DslMarker
annotation class LoadStatesBuilderDsl

@DslMarker
annotation class LoadStateBuilderDsl
