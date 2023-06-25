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

    fun error(): NotificationReference
    fun foregroundInfo(): ForegroundInfo
    fun success(): NotificationReference
}

@Factory
internal class RealSyncNotifications internal constructor(
    private val buildErrorNotification: BuildSyncErrorNotification,
    private val buildForegroundNotification: BuildSyncForegroundNotification,
    private val buildSuccessNotification: BuildSyncSuccessNotification,
    private val notificationManagerCompat: NotificationManagerCompat
) : SyncNotifications {

    override fun error() = NotificationReference(buildErrorNotification(), notificationManagerCompat)
    override fun foregroundInfo() = ForegroundInfo(buildForegroundNotification(), WorkerType.DataSync)
    override fun success() = NotificationReference(buildSuccessNotification(), notificationManagerCompat)
}

@CineScoutTestApi
class FakeSyncNotifications(
    private val foregroundInfo: ForegroundInfo
) : SyncNotifications {

    override fun error() = FakeNotificationReference()
    override fun foregroundInfo() = foregroundInfo
    override fun success() = FakeNotificationReference()
}
