package store

fun StoreKey(id: String) = StoreKey(id, itemId = 0)

@JvmName("TypedStoreKey")
inline fun <reified Model : Any> StoreKey(id: String) = StoreKey("${Model::class.simpleName}$id", itemId = 0)

class StoreKey<Id : Any>(
    private val id: String,
    private val itemId: Id
) {
    @PublishedApi
    internal fun value() = StoreKeyValue(value = "$id($itemId)")

    @PublishedApi
    internal fun paged(paging: Paging.Page) = StoreKey(id = id, itemId = "$itemId#${paging.page}")
}

@JvmInline
value class StoreKeyValue(val value: String)
