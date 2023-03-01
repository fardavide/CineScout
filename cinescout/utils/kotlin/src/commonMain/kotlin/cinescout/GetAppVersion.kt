package cinescout

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

interface GetAppVersion {

    operator fun invoke(): Int
}

@Factory
class RealGetAppVersion(
    @Named(AppVersionQualifier) private val appVersion: Int
) : GetAppVersion {

    override operator fun invoke(): Int = appVersion
}

class FakeGetAppVersion(private val version: Int = 0) : GetAppVersion {

    override operator fun invoke() = version
}
