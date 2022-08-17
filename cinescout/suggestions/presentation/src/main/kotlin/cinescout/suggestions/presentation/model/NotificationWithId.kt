package cinescout.suggestions.presentation.model

import android.app.Notification

data class NotificationWithId(
    val notification: Notification,
    val id: Int
)
