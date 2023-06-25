package cinescout.notification.util

import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ForegroundInfo
import cinescout.notification.model.NotificationWithId

internal fun ForegroundInfo(notificationWithId: NotificationWithId, workerType: WorkerType): ForegroundInfo =
    with(notificationWithId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(id, notification, workerType.platformValue)
        } else {
            ForegroundInfo(id, notification)
        }
    }

internal sealed interface WorkerType {

    @get:RequiresApi(Build.VERSION_CODES.Q)
    val platformValue: Int

    object DataSync : WorkerType {

        @RequiresApi(Build.VERSION_CODES.Q)
        override val platformValue = ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
    }
}
