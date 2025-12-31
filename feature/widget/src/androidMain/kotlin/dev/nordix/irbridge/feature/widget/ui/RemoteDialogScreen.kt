package dev.nordix.irbridge.feature.widget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.feature.widget.domain.model.WidgetControlViewModel
import dev.nordix.irbridge.remotes.commonUi.RemoteItem
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun RemoteDialogScreen(
    remoteId: String,
    fromWidget: Boolean,
    onDismiss: () -> Unit,
) {

    val viewModel = koinViewModel<WidgetControlViewModel> { parametersOf(remoteId) }
    val state by viewModel.state.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onDismiss()
            },
        contentAlignment = Alignment.Center
    ) {
        state.remote?.let { r ->
            RemoteItem(
                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.paddings.large),
                remote = r,
                fromDialog = fromWidget,
                onEditClick = { },
                onDeleteClick = { },
                onDeleteCommandClick = { },
                onSendCommandClick = {
                    viewModel.onCommand(it)
                },
            )
        }
    }
}
