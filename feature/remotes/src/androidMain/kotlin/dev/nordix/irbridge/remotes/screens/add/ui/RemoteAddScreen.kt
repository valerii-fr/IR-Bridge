package dev.nordix.irbridge.remotes.screens.add.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.nordix.irbridge.ble.model.BlePacketUi
import dev.nordix.irbridge.common_ui.text.RDSubtitle
import dev.nordix.irbridge.common_ui.text.RDTextField
import dev.nordix.irbridge.common_ui.text.RDTitle
import dev.nordix.irbridge.common_ui.theme.IRTheme
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.common_ui.theme.spacers
import dev.nordix.irbridge.remotes.R
import dev.nordix.irbridge.remotes.nav.RemotesGraph
import dev.nordix.irbridge.remotes.screens.add.model.RemoteAddEvent
import dev.nordix.irbridge.remotes.screens.add.model.RemoteAddSideEffect
import dev.nordix.irbridge.remotes.screens.add.model.RemoteAddState
import dev.nordix.irbridge.remotes.screens.add.model.RemoteAddViewModel
import dev.nordix.irbridge.remotes.commonUi.RemotePreviewDataProvider.mockCommands
import dev.nordix.irbridge.remotes.commonUi.RemotePreviewDataProvider.mockDurations
import dev.nordix.irbridge.remotes.commonUi.CommandsCard
import dev.nordix.irbridge.remotes.screens.add.ui.composable.PacketCard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RemotesGraph>(navArgs = RemoteAddArgs::class)
@Composable
fun RemoteAddScreen(
    args: RemoteAddArgs?,
    navigator: DestinationsNavigator,
) {
    val viewModel = koinViewModel<RemoteAddViewModel>(
        key = args?.remote?.id?.value,
        parameters = { parametersOf(args?.remote) }
    )
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        navigator.navigateUp()
    }

    SideEffect {
        viewModel.effects.onEach { effect ->
            when (effect) {
                RemoteAddSideEffect.RemoteSaved -> {
                    navigator.navigateUp()
                }
            }
        }
            .launchIn(coroutineScope)
    }

    RemoteAddLayout(
        edit = args?.remote != null,
        state = state,
        onEvent = viewModel::onEvent,
        onBack = navigator::navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RemoteAddLayout(
    edit: Boolean = false,
    state: RemoteAddState,
    onEvent: (RemoteAddEvent) -> Unit,
    onBack: () -> Unit = {},
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    val title = if (edit) {
                        stringResource(R.string.edit_remote, state.remote.name)
                    } else {
                        stringResource(R.string.add_remote)
                    }
                    Text(title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier.padding(pv).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.medium),
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.paddings.medium,
            )
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.medium)
                ) {
                    RDTextField(
                        value = state.remote.name,
                        placeholder = stringResource(R.string.name),
                        onValueChange = {
                            onEvent(
                                RemoteAddEvent.UpdateRemote(
                                    name = it,
                                    description = state.remote.description ?: "",
                                )
                            )
                        },
                        showClearButton = true,
                        error = state.remote.name.isBlank()
                    )

                    RDTextField(
                        value = state.remote.description ?: "",
                        placeholder = stringResource(R.string.description),
                        onValueChange = {
                            onEvent(
                                RemoteAddEvent.UpdateRemote(
                                    name = state.remote.name,
                                    description = it,
                                )
                            )
                        },
                        showClearButton = true,
                    )
                }
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onEvent(RemoteAddEvent.SaveRemote)
                    },
                    enabled = state.remote.name.isNotBlank()
                            && state.remote.commands.isNotEmpty()
                ) {
                    val text = if (state.remote.name.isBlank() || state.remote.commands.isEmpty()) {
                        stringResource(R.string.save_err_no_commands)
                    } else {
                        stringResource(R.string.save)
                    }
                    Text(text)
                }
            }

            if (state.bleConnected) {
                if (state.receivedPattern.isNotEmpty()) {
                    item {
                        Column {
                            PacketCard(
                                BlePacketUi(
                                    tsMillis = System.currentTimeMillis(),
                                    durations = state.receivedPattern
                                ),
                                onSave = {
                                    onEvent(RemoteAddEvent.UpdateRemoteCommands(
                                        state.remote.commands + it
                                    ))
                                },
                                onSend = { onEvent(RemoteAddEvent.SendPattern(it))},
                                onClearPacket = { onEvent(RemoteAddEvent.ClearReceived) }
                            )
                        }
                    }
                } else {
                    item {
                        RDTitle(stringResource(R.string.awaiting_ir))
                    }
                }
            } else {
                item {

                    RDTitle(stringResource(R.string.searching_ble))
                    RDSubtitle(stringResource(R.string.searching_ble_sub))
                }
            }

            item {
                if (state.remote.commands.isNotEmpty()) {
                    CommandsCard(
                        commands = state.remote.commands,
                        onDelete = {
                            onEvent(
                                RemoteAddEvent.UpdateRemoteCommands(
                                    state.remote.commands - it
                                )
                            )
                        },
                        onSendCommand = {
                            onEvent(RemoteAddEvent.SendPattern(it.durations))
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun RemoteAddScreenPreview() {
    IRTheme {
        RemoteAddLayout(
            state = RemoteAddState.newDraft().copy(
                receivedPattern = mockDurations,
                remote = RemoteAddState.newDraft().remote.copy(
                    commands = mockCommands
                )
            ),
            onEvent = {},
        )
    }
}
