package dev.nordix.irbridge.remotes.screens.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import dev.nordix.irbridge.common_ui.misc.ColorsProvider
import dev.nordix.irbridge.common_ui.text.RDSubtitle
import dev.nordix.irbridge.common_ui.text.RDTitle
import dev.nordix.irbridge.common_ui.theme.IRTheme
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.common_ui.theme.spacers
import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.remotes.R
import dev.nordix.irbridge.remotes.domain.model.Remote
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand
import dev.nordix.irbridge.remotes.screens.list.model.RemotesListEvent
import dev.nordix.irbridge.remotes.screens.list.model.RemotesListState

@Composable
internal fun RemotesListLayout(
    modifier: Modifier = Modifier,
    state: RemotesListState,
    onEvent: (RemotesListEvent) -> Unit,
    onEdit: (Remote) -> Unit,
) {

    var showConfirmDeleteDialog by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    showConfirmDeleteDialog?.let { (title, onConfirm) ->
        ConfirmDeleteDialog(
            title = title,
            onDismiss = { showConfirmDeleteDialog = null },
            onConfirm = {
                onConfirm()
                showConfirmDeleteDialog = null
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.small),
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacers.medium)
    ) {
        items(
            items = state.remotes,
            key = { it.id.value },
        ) { item ->
            RemoteItem(
                remote = item,
                onEditClick = { onEdit(item) },
                onDeleteClick = {
                    showConfirmDeleteDialog = item.name to { onEvent(RemotesListEvent.OnRemoteDeleteClicked(item)) }
                },
                onDeleteCommandClick = { onEvent(RemotesListEvent.OnRemoteCommandDeleteClicked(item, it)) },
                onSendCommandClick = { onEvent(RemotesListEvent.OnRemoteCommandSendClicked(item, it)) },
            )
        }
    }
}

@Composable
private fun ConfirmDeleteDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.medium),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.paddings.medium)
            ) {
                RDTitle(stringResource(R.string.delete_s_question, title))
                RDSubtitle(stringResource(R.string.delete_s_question_sub))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun RemotesListLayoutPreview() {
    val commands = List(3) { index ->
        RemoteCommand(
            id = ID(index.toString()),
            name = "Command ${index.unaryPlus()}",
            description = "Description ${index.unaryPlus()}".takeIf { index % 2 == 1 },
            durations = IntArray(1),
            icon = null,
            color = ColorsProvider.commandColors[index % ColorsProvider.commandColors.size].toArgb(),
        )
    }
    val state = RemotesListState(
        remotes = listOf(
            Remote(
                id = ID("1"),
                name = "Remote 1",
                description = "Description 1",
                commands = commands
            ),

        )
    )
    IRTheme {
        RemotesListLayout(
            state = state,
            onEvent = {},
            onEdit = {}
        )
    }
}
