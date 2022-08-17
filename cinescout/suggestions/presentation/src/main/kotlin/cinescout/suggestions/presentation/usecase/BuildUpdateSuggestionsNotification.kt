package cinescout.suggestions.presentation.usecase

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cinescout.suggestions.presentation.model.NotificationWithId
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string

class BuildUpdateSuggestionsNotification(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat
) {

    operator fun invoke(): NotificationWithId {
        val channelId = context.getString(string.suggestions_update_notification_channel_id)
        val notificationId = context.getString(string.suggestions_update_notification_id).toInt()

        val notificationChannel = NotificationChannelCompat.Builder(
            channelId,
            NotificationCompat.PRIORITY_DEFAULT
        )
            .setName(context.getString(string.suggestions_update_notification_channel_name))
            .setDescription(context.getString(string.suggestions_update_notification_channel_description))
            .build()

        val notificationTitle = context.getString(string.suggestions_update_notification_title)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(drawable.ic_movie)
            .setTicker(notificationTitle)
            .setContentTitle(notificationTitle)
            .setContentText(context.getString(string.suggestions_update_notification_content))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        notificationManagerCompat.createNotificationChannel(notificationChannel)

        return NotificationWithId(notification, notificationId)
    }
}
