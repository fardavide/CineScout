package cinescout.network

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class NetworkModule {

    @Factory
    @Named(NetworkQualifier.BaseHttpClient)
    fun baseHttpClient() = CineScoutClient()
}

object NetworkQualifier {

    const val BaseHttpClient = "Base Http client"
    const val IsFirstSourceLinked = "Is first source linked"
    const val IsSecondSourceLinked = "Is second source linked"
}
