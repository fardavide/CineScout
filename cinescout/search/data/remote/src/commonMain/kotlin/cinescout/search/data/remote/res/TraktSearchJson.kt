package cinescout.search.data.remote.res

object TraktSearchJson {

    private val Empty = """
      []
    """.trimIndent()

    fun forQuery(query: String) = when (query) {
        "" -> Empty
        else -> throw UnsupportedOperationException("Unsupported query: $query")
    }
}
