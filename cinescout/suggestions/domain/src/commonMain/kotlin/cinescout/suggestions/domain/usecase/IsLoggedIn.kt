package cinescout.suggestions.domain.usecase

import cinescout.auth.domain.usecase.IsTmdbLinked
import cinescout.auth.domain.usecase.IsTraktLinked
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory

@Factory
class IsLoggedIn(
    private val isTmdbLinked: IsTmdbLinked,
    private val isTraktLinked: IsTraktLinked
) {

    operator fun invoke(): Flow<Boolean> =
        combine(isTmdbLinked(), isTraktLinked()) { tmdbLinked, traktLinked ->
            check(tmdbLinked.not() || traktLinked.not()) {
                "Both accounts are connected: this is not supported"
            }
            tmdbLinked || traktLinked
        }
}
