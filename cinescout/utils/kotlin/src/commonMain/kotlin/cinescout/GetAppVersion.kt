package cinescout

class GetAppVersion(
    private val appVersion: Int
) {

    operator fun invoke(): Int =
        appVersion
}
