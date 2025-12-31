package dev.nordix.irbridge.remotes.screens.add.model

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.nordix.irbridge.ble.data.PermissionsHelper
import dev.nordix.irbridge.ble.domain.NBleClient
import dev.nordix.irbridge.ir.domain.IrTransmitter
import dev.nordix.irbridge.remotes.domain.RemotesRepository
import dev.nordix.irbridge.remotes.domain.model.Remote
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RemoteAddViewModel(
    private val remote: Remote? = null,
    private val remotesRepository: RemotesRepository,
    private val client: NBleClient,
    private val transmitter: IrTransmitter,
    permissionsHelper: PermissionsHelper,
) : ViewModel() {
    private val initState = remote?.let { r ->
        RemoteAddState(
            remote = r,
            receivedPattern = IntArray(0),
            bleReady = false,
            bleConnected = false,
        )
    } ?: RemoteAddState.newDraft()

    private val _state = MutableStateFlow(initState)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<RemoteAddSideEffect>()
    val effects = _effects.asSharedFlow()

    init {
        permissionsHelper.requestBlePermissionsIfNeeded()

        client.isConnected.onEach { isConnected ->
            _state.value = _state.value.copy(bleConnected = isConnected)
        }
            .launchIn(viewModelScope)

        permissionsHelper.bleReady.onEach { bleReady ->
            _state.value = _state.value.copy(bleReady = bleReady)

            coroutineScope {
                @SuppressLint("MissingPermission")
                @Suppress("MagicNumber")
                if (bleReady) {
                    client.getFirstAndListen().onEach { packet ->
                        if (packet.size > 6) {
                            _state.value = _state.value.copy(receivedPattern = packet)
                        }
                    }
                        .launchIn(this)
                }
            }
        }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: RemoteAddEvent) {
        when (event) {
            is RemoteAddEvent.UpdateRemote -> {
                _state.value = _state.value.copy(
                    remote = _state.value.remote.copy(
                        name = event.name,
                        description = event.description,
                    )
                )
            }
            is RemoteAddEvent.SaveRemote -> {
                viewModelScope.launch {
                    remotesRepository.save(_state.value.remote)
                    _effects.emit(RemoteAddSideEffect.RemoteSaved)
                }
            }
            is RemoteAddEvent.SendPattern -> {
                transmitter.transmitFrame(event.durations)
            }
            is RemoteAddEvent.ClearReceived -> {
                _state.value = _state.value.copy(receivedPattern = IntArray(0))
            }
            is RemoteAddEvent.UpdateRemoteCommands -> {
                _state.value = _state.value.copy(
                    remote = _state.value.remote.copy(
                        commands = event.commands
                    )
                )
            }
        }
    }

}
