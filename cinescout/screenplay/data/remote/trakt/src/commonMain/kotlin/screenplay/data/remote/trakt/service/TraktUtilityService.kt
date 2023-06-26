package screenplay.data.remote.trakt.service

import arrow.core.Either
import arrow.core.Nel
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.utils.kotlin.nonEmptyUnsafe
import cinescout.utils.kotlin.plus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import screenplay.data.remote.trakt.model.TraktGenre

interface TraktUtilityService {

    suspend fun getGenres(): Either<NetworkError, Nel<TraktGenre>>
}

@Factory
internal class RealTraktUtilityService(
    @Named(TraktClient) private val client: HttpClient
) : TraktUtilityService {

    override suspend fun getGenres(): Either<NetworkError, Nel<TraktGenre>> =
        (getMovieGenres() + getTvShowGenres()).map { list -> list.distinct().nonEmptyUnsafe() }

    private suspend fun getMovieGenres(): Either<NetworkError, List<TraktGenre>> =
        Either.Try { client.get { url.path("genres", "movies") }.body() }

    private suspend fun getTvShowGenres(): Either<NetworkError, List<TraktGenre>> =
        Either.Try { client.get { url.path("genres", "shows") }.body() }
}
