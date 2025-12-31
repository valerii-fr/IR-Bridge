package dev.nordix.irbridge.remotes.commonUi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nordix.irbridge.common_ui.card.SwipeableContainer
import dev.nordix.irbridge.common_ui.card.common.RDCardView
import dev.nordix.irbridge.common_ui.text.RDTextBlock
import dev.nordix.irbridge.common_ui.theme.IRTheme
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.remotes.R
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand
import dev.nordix.irbridge.remotes.commonUi.RemotePreviewDataProvider.mockCommands

@Composable
internal fun CommandsCard(
    commands: List<RemoteCommand>,
    onDelete: ((RemoteCommand) -> Unit)?,
    onSendCommand: (RemoteCommand) -> Unit,
    showTitle: Boolean = true,
) {
    RDCardView(key = commands) {
        if (showTitle) {
            title(stringResource(R.string.saved_commands))
        }
        commands.forEach { command ->
            raw {
                CommandItemContent(
                    command = command,
                    onDelete = onDelete,
                    onSendCommand = onSendCommand
                )
            }
        }
    }

}

@Composable
internal fun CommandItemContent(
    command: RemoteCommand,
    swipeEnabled: Boolean = true,
    onDelete: ((RemoteCommand) -> Unit)?,
    onSendCommand: (RemoteCommand) -> Unit,
) {
    SwipeableContainer(
        key = command,
        swipeEnabled = swipeEnabled,
        modifier = Modifier.fillMaxWidth(),
        actionsEnd = {
            onDelete?.let {
                IconButton(
                    onClick = { onDelete(command) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_command)
                    )
                }
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clickable(
                    enabled = true,
                    onClick = { onSendCommand(command) },
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                )
                .padding(
                    horizontal = MaterialTheme.paddings.medium,
                    vertical = MaterialTheme.paddings.small
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RDTextBlock(
                modifier = Modifier.weight(HALF),
                text = command.name,
                supportingText = command.description,
            )
            IrDurationsChart(
                durationsUs = command.durations,
                maxWidth = 80.dp,
                lineColor = command.color?.let(::Color) ?: MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(HALF, fill = false)
                    .height(40.dp)
                    .padding(start = MaterialTheme.paddings.medium)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CommandCardPreview() {
    IRTheme {
        CommandsCard(
            commands = mockCommands,
            onDelete = {},
            onSendCommand = {}
        )
    }
}

private const val HALF = 0.5f
