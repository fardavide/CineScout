package cinescout.suggestions.domain.usecase

import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.auth.trakt.domain.usecase.IsTraktLinked
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
            tmdbLinked || traktLinked
        }
}
