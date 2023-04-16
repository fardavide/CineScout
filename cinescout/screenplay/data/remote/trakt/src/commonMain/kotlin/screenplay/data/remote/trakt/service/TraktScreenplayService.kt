package screenplay.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.toTraktQueryString
import cinescout.screenplay.domain.model.TraktScreenplayId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody

interface TraktScreenplayService {

    suspend fun getScreenplay(id: TraktScreenplayId): Either<NetworkError, TraktScreenplayExtendedBody>
}

@Factory
internal class RealTraktScreenplayService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) : TraktScreenplayService {

    override suspend fun getScreenplay(
        id: TraktScreenplayId
    ): Either<NetworkError, TraktScreenplayExtendedBody> = Either.Try {
        client.get {
            url {
                path(id.toTraktQueryString(), id.value.toString())
                extendedParameter(TraktExtended.Full)
            }
        }.body()
    }
}
