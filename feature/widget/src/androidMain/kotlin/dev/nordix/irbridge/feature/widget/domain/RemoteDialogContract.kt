package dev.nordix.irbridge.feature.widget.domain

import dev.nordix.irbridge.feature.widget.WidgetControlActivity

object RemoteDialogContract {
    const val ACTION_OPEN_REMOTE_DIALOG = "dev.nordix.irbridge.action.OPEN_REMOTE_DIALOG"

    const val EXTRA_REMOTE_ID = "extra_remote_id"
    const val EXTRA_FROM_WIDGET = "extra_from_widget"

    fun createIntent(context: android.content.Context, remoteId: String, fromWidget: Boolean = false) =
        android.content.Intent(context, WidgetControlActivity::class.java).apply {
            action = ACTION_OPEN_REMOTE_DIALOG
            putExtra(EXTRA_REMOTE_ID, remoteId)
            putExtra(EXTRA_FROM_WIDGET, fromWidget)
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
}
