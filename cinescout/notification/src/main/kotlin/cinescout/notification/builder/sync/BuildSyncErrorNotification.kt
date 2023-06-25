package cinescout.notification.builder.sync

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cinescout.notification.model.NotificationWithId
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import org.koin.core.annotation.Factory

@Factory
internal class BuildSyncErrorNotification(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val createSyncGroup: CreateSyncGroup
) {

    operator fun invoke(): NotificationWithId {
        val channelId = context.getString(string.sync_error_channel_id)
        val notificationId = context.getString(string.sync_error_notification_id).toInt()

        createChannel(channelId, createSyncGroup())

        val notificationTitle = context.getString(string.sync_error_notification_title)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(drawable.ic_movie)
            .setTicker(notificationTitle)
            .setContentTitle(notificationTitle)
            .setContentText(context.getString(string.sync_error_notification_content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        return NotificationWithId(notification, notificationId)
    }

    private fun createChannel(channelId: String, groupId: String) {
        val channel = NotificationChannelCompat.Builder(
            channelId,
            NotificationCompat.PRIORITY_HIGH
        )
            .setGroup(groupId)
            .setName(context.getString(string.sync_error_channel_name))
            .setDescription(context.getString(string.sync_error_channel_description))
            .build()

        notificationManagerCompat.createNotificationChannel(channel)
    }
}
