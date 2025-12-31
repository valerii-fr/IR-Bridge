package dev.nordix.irbridge.feature.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import dev.nordix.irbridge.feature.widget.ui.RemoteWidgetContent
import dev.nordix.irbridge.remotes.domain.RemotesRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.java.KoinJavaComponent.inject

class RemoteWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val remotesRepository by inject<RemotesRepository>(RemotesRepository::class.java)
        val remotesFlow = remotesRepository
            .observeAll()
            .distinctUntilChanged()

        provideContent {
            val remotes by remotesFlow.collectAsState(emptyList())
            key(remotes) {
                RemoteWidgetContent(
                    remotes = remotes
                )
            }
        }
    }

}
