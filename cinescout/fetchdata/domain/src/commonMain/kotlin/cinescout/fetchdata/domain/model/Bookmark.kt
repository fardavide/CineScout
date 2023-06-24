package cinescout.fetchdata.domain.model

sealed interface Bookmark {

    object None : Bookmark
}

@JvmInline
value class Page(val value: Int) : Bookmark

sealed interface DataStatus : Bookmark {
    object InitialFetched : DataStatus
    object CompleteFetched : DataStatus
}
