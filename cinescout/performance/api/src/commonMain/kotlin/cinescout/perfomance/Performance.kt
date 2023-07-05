package cinescout.perfomance

interface Performance {

    suspend fun <T> trace(traceName: String, block: suspend (Trace) -> T): T
}
