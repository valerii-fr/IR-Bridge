package dev.nordix.irbridge.feature.widget.domain

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import dev.nordix.irbridge.feature.widget.RemoteWidgetReceiver

object WidgetUpdateBridge {

    fun requestUpdate(context: Context) {
        val appContext = context.applicationContext
        val manager = AppWidgetManager.getInstance(appContext)
        val component = ComponentName(appContext, RemoteWidgetReceiver::class.java)

        val widgetIds = manager.getAppWidgetIds(component)
        if (widgetIds.isEmpty()) return

        manager.notifyAppWidgetViewDataChanged(widgetIds, android.R.id.list)

        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
        }
        appContext.sendBroadcast(updateIntent)
    }
}