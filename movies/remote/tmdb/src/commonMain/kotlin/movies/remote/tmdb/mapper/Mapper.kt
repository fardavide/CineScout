package movies.remote.tmdb.mapper

import entities.Either
import entities.Error
import entities.Invokable
import entities.NetworkError
import entities.flatMap
import entities.invoke
import entities.right

interface Mapper<T, V> : Invokable {

    suspend fun T.toBusinessModel(): Either<NetworkError, V>

}

// Collection
inline fun <T, V, M : Mapper<T, V>> Collection<T>.map(
    mapper: M,
    map: M.(T) -> Either<NetworkError, V>
): Either<NetworkError, List<V>> = Either.fix {
    map {
        val (value) = mapper { map(it) }
        value
    }.right()
}

// Either
inline fun <T, E : Error, V, M : Mapper<T, V>> Either<E, T>.map(
    mapper: M,
    map: M.(T) -> V
): Either<E, V> = map { mapper { map(it) } }

inline fun <T, V, M : Mapper<T, V>> Either<NetworkError, T>.flatMap(
    mapper: M,
    map: M.(T) -> Either<NetworkError, V>
): Either<NetworkError, V> = flatMap { mapper { map(it) } }
