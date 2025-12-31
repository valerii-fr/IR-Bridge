package dev.nordix.irbridge.feature.widget.domain.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.ir.domain.IrTransmitter
import dev.nordix.irbridge.remotes.domain.RemotesRepository
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class WidgetControlViewModel(
    private val remoteId: String,
    private val remotesRepository: RemotesRepository,
    private val transmitter: IrTransmitter,
) : ViewModel() {
    private val _state = MutableStateFlow(WidgetControlState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val remote = remotesRepository.getById(ID(remoteId))
            _state.value = WidgetControlState(remote = remote)
        }
    }

    fun onCommand(command: RemoteCommand) {
        transmitter.transmitFrame(command.durations)
    }

}