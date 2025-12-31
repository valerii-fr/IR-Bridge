package dev.nordix.irbridge.remotes.screens.add.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import dev.nordix.irbridge.ble.model.BlePacketUi
import dev.nordix.irbridge.common_ui.card.common.RDCardView
import dev.nordix.irbridge.common_ui.misc.ColorsProvider
import dev.nordix.irbridge.common_ui.text.RDTextField
import dev.nordix.irbridge.common_ui.text.RDTitle
import dev.nordix.irbridge.common_ui.theme.IRTheme
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.common_ui.theme.spacers
import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.core.utils.formatTime
import dev.nordix.irbridge.remotes.R
import dev.nordix.irbridge.remotes.commonUi.IrDurationsChart
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand
import dev.nordix.irbridge.remotes.screens.add.ui.RemoteAddPreviewDataProvider.blePacketUi
import dev.nordix.irbridge.remotes.screens.add.ui.RemoteAddPreviewDataProvider.mockDurations

@Composable
internal fun PacketCard(
    p: BlePacketUi,
    onSave: (command: RemoteCommand) -> Unit,
    onSend: (durations: IntArray) -> Unit,
    onClearPacket: () -> Unit,
) {
    val time = remember(p.tsMillis) { formatTime(p.tsMillis) }
    var showDurationsText by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }

    if (showSaveDialog) {
        SaveDialog(
            durations = p.durations,
            onSave = {
                onSave(it)
                showSaveDialog = false
            },
            onDismiss = { showSaveDialog = false }
        )
    }

    RDCardView(
        key = p.hashCode() + showDurationsText.hashCode()
    ) {
        title(stringResource(R.string.received_packet))
        textValue(label = stringResource(R.string.time), text = time)
        raw {
            IrDurationsChart(
                durationsUs = p.durations,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacers.xxLarge)
                    .padding(horizontal = MaterialTheme.paddings.medium)
                    .clickable(
                        enabled = true,
                        onClick = { showDurationsText = !showDurationsText },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }
        if (showDurationsText) {
            text(
                text = stringResource(R.string.durations, p.durations.joinToString("|")),
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace
            )
        }
        button(
            text = stringResource(R.string.send_packet),
            subtitle = stringResource(R.string.verify),
            buttonText = stringResource(R.string.go),
            onClick = { onSend(p.durations) }
        )
        button(
            text = stringResource(R.string.save_command),
            subtitle = stringResource(R.string.save_command_sub),
            buttonText = stringResource(R.string.save),
            onClick = { showSaveDialog = true }
        )
        button(
            text = stringResource(R.string.received),
            subtitle = stringResource(R.string.clear_received),
            buttonText = stringResource(R.string.clear),
            onClick = onClearPacket
        )
    }
}

@Composable
private fun SaveDialog(
    durations: IntArray,
    onSave: (command: RemoteCommand) -> Unit,
    onDismiss: () -> Unit,
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val primaryColor = MaterialTheme.colorScheme.primary
    var commandColor by remember { mutableIntStateOf(primaryColor.toArgb()) }
    var icon by remember { mutableStateOf<Int?>(null) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(MaterialTheme.paddings.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.medium)
            ) {
                RDTitle(stringResource(R.string.save_new_command))
                RDTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = stringResource(R.string.name),
                    showClearButton = true,
                    error = name.isBlank()
                )
                RDTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.description),
                    showClearButton = true,
                )

                var showColorSelector by remember {
                    mutableStateOf(false)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.spacers.extraLarge),
                    shape = CircleShape,
                    color = Color(commandColor),
                    onClick = { showColorSelector = !showColorSelector }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.choose_color),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                    DropdownMenu(
                        expanded = showColorSelector,
                        onDismissRequest = { showColorSelector = false }
                    ) {
                        ColorsProvider.commandColors.forEach { color ->
                            DropdownMenuItem(
                                modifier = Modifier.background(color),
                                text = {
                                    Text(color.toArgb().toHexString())
                                },
                                colors = MenuDefaults.itemColors().copy(
                                    textColor = MaterialTheme.colorScheme.surface
                                ),
                                onClick = {
                                    commandColor = color.toArgb()
                                    showColorSelector = false
                                }
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            onSave(
                                RemoteCommand(
                                    id = ID.new(),
                                    name = name,
                                    description = description,
                                    icon = icon,
                                    color = commandColor,
                                    durations = durations
                                )
                            )
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Text(stringResource(R.string.save))
                    }

                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SaveDialogPreview() {
    IRTheme {
        SaveDialog(
            durations = mockDurations,
            onSave = {},
            onDismiss = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PacketCardPreview() {
    IRTheme {
        PacketCard(
            p = blePacketUi,
            onSave = {},
            onSend = {},
            onClearPacket = {}
        )
    }
}
