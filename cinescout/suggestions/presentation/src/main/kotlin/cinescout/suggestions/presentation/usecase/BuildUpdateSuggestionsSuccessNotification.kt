package cinescout.suggestions.presentation.usecase

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.suggestions.presentation.model.NotificationWithId
import org.koin.core.annotation.Factory

@Factory
class BuildUpdateSuggestionsSuccessNotification(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val createUpdateSuggestionsGroup: CreateUpdateSuggestionsGroup
) {

    operator fun invoke(): NotificationWithId {
        val channelId = context.getString(string.suggestions_update_success_channel_id)
        val notificationId = context.getString(string.suggestions_update_success_notification_id).toInt()

        createChannel(channelId, createUpdateSuggestionsGroup())

        val notificationTitle = context.getString(string.suggestions_update_success_notification_title)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(drawable.ic_movie)
            .setTicker(notificationTitle)
            .setContentTitle(notificationTitle)
            .setContentText(context.getString(string.suggestions_update_success_notification_content))
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
            .setName(context.getString(string.suggestions_update_success_channel_name))
            .setDescription(context.getString(string.suggestions_update_success_channel_description))
            .build()

        notificationManagerCompat.createNotificationChannel(channel)
    }
}
