package movies.remote.tmdb.mapper

import entities.Either
import entities.Error
import entities.Invokable
import entities.invoke
import entities.then

interface Mapper<T, V> : Invokable {

    suspend fun <E : Error> Either<E, T>.toBusinessModel(): Either<E, V> =
        map { it.toBusinessModel() }

    suspend fun T.toBusinessModel(): V
}

// Collection
inline fun <T, V, M : Mapper<T, V>> Collection<T>.map(mapper: M, map: M.(T) -> V): List<V> =
    map { mapper { map(it) } }

// Either
inline fun <E : Error, T, V> Either<E, T>.map(mapper: Mapper<T, V>, map: Mapper<T, V>.(T) -> V): Either<E, V> =
    map { mapper { map(it) } }

// Result
inline fun <T, V> Result<T>.mapOrNull(mapper: Mapper<T, V>, map: Mapper<T, V>.(T) -> V): V? =
    map { mapper { map(it) } }.getOrNull()
