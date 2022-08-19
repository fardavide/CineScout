package store

import kotlin.reflect.KClass

inline fun <reified T : Any, Id : Any> StoreKey(id: Id) = StoreKey(T::class, id)
inline fun <reified T : Any> StoreKey() = StoreKey(T::class, 0)

data class StoreKey<T : Any, Id : Any>(val type: KClass<T>, val id: Id) {
    fun value() = StoreKeyValue(value = "${type.simpleName}($id)")
}

@JvmInline
value class StoreKeyValue(val value: String)
