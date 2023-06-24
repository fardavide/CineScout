package cinescout.database.model

sealed interface DatabaseBookmark {
    object None : DatabaseBookmark
}

@JvmInline
value class DatabasePage(val value: Int) : DatabaseBookmark

sealed interface DatabaseDataStatus : DatabaseBookmark {
    object InitialFetched : DatabaseDataStatus
    object CompleteFetched : DatabaseDataStatus
}
