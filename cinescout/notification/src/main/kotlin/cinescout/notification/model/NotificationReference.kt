package cinescout.notification.model

import androidx.core.app.NotificationManagerCompat
import cinescout.CineScoutTestApi

interface NotificationReference {

    fun show()
}

internal fun NotificationReference(
    notificationWithId: NotificationWithId,
    notificationManagerCompat: NotificationManagerCompat
) = RealNotificationReference(notificationWithId, notificationManagerCompat)

internal data class RealNotificationReference(
    private val notification: NotificationWithId,
    private val notificationManagerCompat: NotificationManagerCompat
) : NotificationReference {

    override fun show() {
        notificationManagerCompat.notify(notification.id, notification.notification)
    }
}

@CineScoutTestApi
class FakeNotificationReference : NotificationReference {

    override fun show() = Unit
}
