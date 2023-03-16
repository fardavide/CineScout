package cinescout.fetchdata.domain.model

sealed interface FetchKey {

    data class WithoutPage<T : Any>(val value: T) : FetchKey

    data class WithPage<T : Any>(val value: T, val page: Int) : FetchKey
}
