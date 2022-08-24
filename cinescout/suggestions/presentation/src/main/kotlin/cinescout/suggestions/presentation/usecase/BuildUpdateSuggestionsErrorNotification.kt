package cinescout.suggestions.presentation.usecase

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cinescout.suggestions.presentation.model.NotificationWithId
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string

class BuildUpdateSuggestionsErrorNotification(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val createUpdateSuggestionsGroup: CreateUpdateSuggestionsGroup
) {

    operator fun invoke(): NotificationWithId {
        val channelId = context.getString(string.suggestions_update_error_channel_id)
        val notificationId = context.getString(string.suggestions_update_error_notification_id).toInt()

        createChannel(channelId, createUpdateSuggestionsGroup())

        val notificationTitle = context.getString(string.suggestions_update_error_notification_title)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(drawable.ic_movie)
            .setTicker(notificationTitle)
            .setContentTitle(notificationTitle)
            .setContentText(context.getString(string.suggestions_update_error_notification_content))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return NotificationWithId(notification, notificationId)
    }

    private fun createChannel(channelId: String, groupId: String) {
        val channel = NotificationChannelCompat.Builder(
            channelId,
            NotificationCompat.PRIORITY_DEFAULT
        )
            .setGroup(groupId)
            .setName(context.getString(string.suggestions_update_error_channel_name))
            .setDescription(context.getString(string.suggestions_update_error_channel_description))
            .build()

        notificationManagerCompat.createNotificationChannel(channel)
    }
}
