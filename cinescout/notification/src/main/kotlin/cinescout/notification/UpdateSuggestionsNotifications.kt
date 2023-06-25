package cinescout.notification

import androidx.core.app.NotificationManagerCompat
import androidx.work.ForegroundInfo
import cinescout.CineScoutTestApi
import cinescout.notification.builder.suggestion.BuildUpdateSuggestionsErrorNotification
import cinescout.notification.builder.suggestion.BuildUpdateSuggestionsForegroundNotification
import cinescout.notification.builder.suggestion.BuildUpdateSuggestionsSuccessNotification
import cinescout.notification.model.FakeNotificationReference
import cinescout.notification.model.NotificationReference
import cinescout.notification.util.ForegroundInfo
import cinescout.notification.util.WorkerType
import org.koin.core.annotation.Factory

interface UpdateSuggestionsNotifications {

    fun error(): NotificationReference
    fun foregroundInfo(): ForegroundInfo
    fun success(): NotificationReference
}

@Factory
internal class RealUpdateSuggestionsNotifications internal constructor(
    private val buildErrorNotification: BuildUpdateSuggestionsErrorNotification,
    private val buildForegroundNotification: BuildUpdateSuggestionsForegroundNotification,
    private val buildSuccessNotification: BuildUpdateSuggestionsSuccessNotification,
    private val notificationManagerCompat: NotificationManagerCompat
) : UpdateSuggestionsNotifications {

    override fun error() = NotificationReference(buildErrorNotification(), notificationManagerCompat)
    override fun foregroundInfo() = ForegroundInfo(buildForegroundNotification(), WorkerType.DataSync)
    override fun success() = NotificationReference(buildSuccessNotification(), notificationManagerCompat)
}

@CineScoutTestApi
class FakeUpdateSuggestionsNotifications(
    private val foregroundInfo: ForegroundInfo
) : UpdateSuggestionsNotifications {

    override fun error() = FakeNotificationReference()
    override fun foregroundInfo() = foregroundInfo
    override fun success() = FakeNotificationReference()
}
