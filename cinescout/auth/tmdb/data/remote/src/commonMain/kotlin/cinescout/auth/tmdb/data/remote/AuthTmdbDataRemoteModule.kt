package cinescout.auth.tmdb.data.remote

import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.network.NetworkQualifier
import cinescout.network.tmdb.TmdbNetworkQualifier
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class AuthTmdbDataRemoteModule {

    @Factory
    @Named(NetworkQualifier.IsFirstSourceLinked)
    fun isFirstSourceLinked(isTmdbLinked: IsTmdbLinked) = suspend { isTmdbLinked().first() }

    @Factory
    @Named(TmdbNetworkQualifier.RedirectUrl)
    fun redirectUrl() = TmdbRedirectUrl
}

internal const val TmdbRedirectUrl = "cinescout://tmdb"

internal fun getTmdbAuthorizeTokenUrl(requestToken: TmdbRequestToken) =
    "https://www.themoviedb.org/auth/access?request_token=${requestToken.value}"


