package cinescout.notification.builder.sync

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cinescout.notification.model.NotificationWithId
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import org.koin.core.annotation.Factory

@Factory
internal class BuildSyncSuccessNotification(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val createSyncGroup: CreateSyncGroup
) {

    operator fun invoke(result: String): NotificationWithId {
        val channelId = context.getString(string.sync_success_channel_id)
        val notificationId = context.getString(string.sync_success_notification_id).toInt()

        createChannel(channelId, createSyncGroup())

        val notificationTitle = context.getString(string.sync_success_notification_title)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(drawable.ic_movie)
            .setTicker(notificationTitle)
            .setContentTitle(notificationTitle)
            .setStyle(NotificationCompat.BigTextStyle().bigText(styled(result)))
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
            .setName(context.getString(string.sync_success_channel_name))
            .setDescription(context.getString(string.sync_success_channel_description))
            .build()

        notificationManagerCompat.createNotificationChannel(channel)
    }

    private fun styled(result: String): CharSequence {
        val lines = result.split("\n")
        val spannableBuilder = SpannableStringBuilder()

        for (line in lines) {
            val colonIndex = line.indexOf(":")
            if (colonIndex != -1) {
                val spannableString = SpannableString(line)
                spannableString.setSpan(
                    StyleSpan(android.graphics.Typeface.BOLD),
                    0,
                    colonIndex + 1,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableBuilder.append(spannableString)
            } else {
                spannableBuilder.append(line)
            }
            spannableBuilder.append("\n")
        }

        return spannableBuilder.trimEnd()
    }
}
