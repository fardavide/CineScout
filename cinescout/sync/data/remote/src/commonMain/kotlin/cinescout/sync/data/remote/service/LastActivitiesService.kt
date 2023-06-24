package cinescout.sync.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.sync.data.remote.model.TraktLastActivities
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class LastActivitiesService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getLastActivities(): Either<NetworkError, TraktLastActivities> = Either.Try {
        client.get { url.path("sync", "last_activities") }.body()
    }
}
