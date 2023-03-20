package cinescout.suggestions.presentation.usecase

import android.content.Context
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationManagerCompat
import cinescout.resources.R.string
import org.koin.core.annotation.Factory

@Factory
class CreateUpdateSuggestionsGroup(
    private val context: Context,
    private val notificationManagerCompat: NotificationManagerCompat
) {

    operator fun invoke(): String {
        val groupId = context.getString(string.suggestions_update_group_id)
        val group = NotificationChannelGroupCompat.Builder(groupId)
            .setName(context.getString(string.suggestions_update_group_name))
            .setDescription(context.getString(string.suggestions_update_group_description))
            .build()

        notificationManagerCompat.createNotificationChannelGroup(group)

        return groupId
    }
}
