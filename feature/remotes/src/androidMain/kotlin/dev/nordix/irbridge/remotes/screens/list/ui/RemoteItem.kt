package dev.nordix.irbridge.remotes.screens.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.nordix.irbridge.common_ui.card.SwipeableContainer
import dev.nordix.irbridge.common_ui.card.common.RDCardView
import dev.nordix.irbridge.common_ui.theme.spacers
import dev.nordix.irbridge.remotes.R
import dev.nordix.irbridge.remotes.commonUi.CommandItemContent
import dev.nordix.irbridge.remotes.domain.model.Remote
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand

@Composable
internal fun RemoteItem(
    modifier: Modifier = Modifier,
    remote: Remote,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDeleteCommandClick: (RemoteCommand) -> Unit,
    onSendCommandClick: (RemoteCommand) -> Unit,
) {
    var showCommands by remember { mutableStateOf(false) }

    SwipeableContainer(
        modifier = modifier,
        actionsEnd = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.medium)
            ) {
                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_remote)
                    )
                }
                IconButton(
                    onClick = onEditClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_remote, remote.name)
                    )
                }
            }
        }
    ) {
        RDCardView(
            key = remote.id.value.hashCode() + showCommands.hashCode(),
        ) {
            textBlock(
                text = remote.name,
                supportingText = remote.description
            )
            if (remote.commands.isNotEmpty()) {
                navigation(
                    text = if (!showCommands) {
                        stringResource(R.string.show_commands)
                    } else {
                        stringResource(R.string.hide_commands)
                    },
                    onClick = { showCommands = !showCommands }
                )
                if (showCommands) {
                    remote.commands.forEach { command ->
                        raw {
                            CommandItemContent(
                                command = command,
                                onDelete = onDeleteCommandClick,
                                onSendCommand = onSendCommandClick,
                                swipeEnabled = false,
                            )
                        }
                    }
                }
            }
        }
    }

}
