package dev.nordix.irbridge.feature.widget.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.feature.widget.domain.RemoteDialogContract
import dev.nordix.irbridge.remotes.domain.model.Remote

@Composable
internal fun RemoteWidgetContent(
    remotes: List<Remote> = emptyList()
) {

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = GlanceModifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
    ) {
        remotes.forEach { (id, name, description) ->
            Column(
                modifier = GlanceModifier.padding(16.dp)
            ) {
                Button(
                    modifier = GlanceModifier.defaultWeight(),
                    text = name,
                    onClick = {
                        val i = RemoteDialogContract.createIntent(
                            context = context,
                            remoteId = id.value,
                            fromWidget = true
                        )
                        context.startActivity(i)
                    }
                )
                description?.let {
                    Text(text = description)
                }
            }
        }
    }
}