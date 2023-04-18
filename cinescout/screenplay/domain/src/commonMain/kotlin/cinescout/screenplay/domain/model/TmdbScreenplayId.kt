package cinescout.screenplay.domain.model

import cinescout.unsupported
import kotlinx.serialization.Serializable

sealed interface TmdbScreenplayId {

    val value: Int

    @JvmInline
    @Serializable
    value class Movie(override val value: Int) : TmdbScreenplayId

    @JvmInline
    @Serializable
    value class TvShow(override val value: Int) : TmdbScreenplayId

    companion object {

        inline fun <reified T : TmdbScreenplayId> invalid(): T = when (T::class) {
            Movie::class -> Movie(-1) as T
            TvShow::class -> TvShow(-1) as T
            else -> unsupported
        }
    }
}
