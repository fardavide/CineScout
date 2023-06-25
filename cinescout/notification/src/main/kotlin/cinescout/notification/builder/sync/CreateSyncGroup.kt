package cinescout.notification.builder.sync

import android.content.Context
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationManagerCompat
import cinescout.resources.R.string
import org.koin.core.annotation.Factory

@Factory
internal class CreateSyncGroup(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat
) {

    operator fun invoke(): String {
        val groupId = context.getString(string.sync_group_id)
        val group = NotificationChannelGroupCompat.Builder(groupId)
            .setName(context.getString(string.sync_group_name))
            .setDescription(context.getString(string.sync_group_description))
            .build()

        notificationManagerCompat.createNotificationChannelGroup(group)

        return groupId
    }
}
