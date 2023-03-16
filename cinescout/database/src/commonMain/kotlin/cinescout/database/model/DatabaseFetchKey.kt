package cinescout.database.model

sealed interface DatabaseFetchKey {

    val value: String

    data class WithoutPage(override val value: String) : DatabaseFetchKey

    data class WithPage(override val value: String, val page: Int) : DatabaseFetchKey
}
