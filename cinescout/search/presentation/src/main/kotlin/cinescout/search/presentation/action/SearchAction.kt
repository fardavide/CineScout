package cinescout.search.presentation.action

internal sealed interface SearchAction {

    @JvmInline
    value class Search(val query: String) : SearchAction
}
