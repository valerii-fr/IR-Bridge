package dev.nordix.irbridge.remotes.screens.list.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.nordix.irbridge.ir.domain.IrTransmitter
import dev.nordix.irbridge.remotes.domain.RemotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RemotesListScreenViewModel(
    private val remotesRepository: RemotesRepository,
    private val transmitter: IrTransmitter,
) : ViewModel() {
    private val _state = MutableStateFlow(RemotesListState(remotes = emptyList()))
    val state = _state.asStateFlow()

    init {
        remotesRepository.observeAll().onEach { remotes ->
            _state.value = _state.value.copy(remotes = remotes)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: RemotesListEvent) {
        when (event) {
            is RemotesListEvent.OnRemoteDeleteClicked -> {
                viewModelScope.launch {
                    remotesRepository.delete(event.remote)
                }
            }
            is RemotesListEvent.OnRemoteCommandDeleteClicked -> {
                viewModelScope.launch {
                    remotesRepository.save(
                        event.remote.copy(commands = event.remote.commands - event.command)
                    )
                }
            }

            is RemotesListEvent.OnRemoteCommandSendClicked -> {
                transmitter.transmitFrame(event.command.durations)
            }
        }
    }
}
