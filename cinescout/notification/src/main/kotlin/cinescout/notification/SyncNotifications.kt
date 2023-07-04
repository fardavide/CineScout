package cinescout.notification

import androidx.core.app.NotificationManagerCompat
import androidx.work.ForegroundInfo
import cinescout.CineScoutTestApi
import cinescout.notification.builder.sync.BuildSyncErrorNotification
import cinescout.notification.builder.sync.BuildSyncForegroundNotification
import cinescout.notification.builder.sync.BuildSyncSuccessNotification
import cinescout.notification.model.FakeNotificationReference
import cinescout.notification.model.NotificationReference
import cinescout.notification.util.ForegroundInfo
import cinescout.notification.util.WorkerType
import org.koin.core.annotation.Factory

interface SyncNotifications {

    fun error(result: String): NotificationReference
    fun foregroundInfo(): ForegroundInfo
    fun success(result: String): NotificationReference
}

@Factory
internal class RealSyncNotifications internal constructor(
    private val buildErrorNotification: BuildSyncErrorNotification,
    private val buildForegroundNotification: BuildSyncForegroundNotification,
    private val buildSuccessNotification: BuildSyncSuccessNotification,
    private val notificationManagerCompat: NotificationManagerCompat
) : SyncNotifications {

    override fun error(result: String) =
        NotificationReference(buildErrorNotification(result), notificationManagerCompat)
    override fun foregroundInfo() = ForegroundInfo(buildForegroundNotification(), WorkerType.DataSync)
    override fun success(result: String) =
        NotificationReference(buildSuccessNotification(result), notificationManagerCompat)
}

@CineScoutTestApi
class FakeSyncNotifications(
    private val foregroundInfo: ForegroundInfo
) : SyncNotifications {

    override fun error(result: String) = FakeNotificationReference()
    override fun foregroundInfo() = foregroundInfo
    override fun success(result: String) = FakeNotificationReference()
}
