package store

import kotlin.reflect.KClass

inline fun <reified T : Any, Id : Any> StoreKey(id: Id) = StoreKey(T::class, id)
inline fun <reified T : Any> StoreKey() = StoreKey(T::class, 0)

class StoreKey<T : Any, Id : Any> @PublishedApi internal constructor(
    private val type: KClass<T>,
    private val id: Id
) {
    @PublishedApi
    internal fun value() = StoreKeyValue(value = "${type.simpleName}($id)")

    @PublishedApi
    internal fun <P : Paging.Page> paged(paging: P) = StoreKey(type = type, id = "$id#${paging.page}")
}

@JvmInline
value class StoreKeyValue(val value: String)
