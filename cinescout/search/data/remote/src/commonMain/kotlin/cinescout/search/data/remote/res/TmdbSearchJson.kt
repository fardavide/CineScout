package cinescout.search.data.remote.res

object TmdbSearchJson {

    private val Empty = """
        {
            "page": 1,
            "results": [],
            "total_pages": 1,
            "total_results": 0
        }
    """.trimIndent()

    fun forQuery(query: String) = when (query) {
        "" -> Empty
        else -> throw UnsupportedOperationException("Unsupported query: $query")
    }
}
