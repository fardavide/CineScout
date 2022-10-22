package cinescout.utils.android

interface Reducer<State, in Operation> {

    fun State.reduce(operation: Operation): State
}
