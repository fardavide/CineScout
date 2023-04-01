package cinescout

import org.koin.core.annotation.Factory

interface GetAppVersion {

    operator fun invoke(): Int
}

@Factory
class RealGetAppVersion(
    @AppVersionQualifier private val appVersion: Int
) : GetAppVersion {

    override operator fun invoke(): Int = appVersion
}

class FakeGetAppVersion(private val version: Int = 0) : GetAppVersion {

    override operator fun invoke() = version
}
