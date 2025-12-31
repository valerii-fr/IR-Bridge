package dev.nordix.irbridge.remotes.screens.add.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.nordix.irbridge.ble.model.BlePacketUi
import dev.nordix.irbridge.common_ui.misc.ColorsProvider
import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand

internal object RemoteAddPreviewDataProvider {
    val blePacketUi = BlePacketUi(
        tsMillis = System.currentTimeMillis(),
        durations = IntArray(40) { idx ->
            idx % 4
        }
    )

    val mockDurations = IntArray(40) { idx ->
        idx % 4
    }

    val mockCommands = List(4) { idx ->
        RemoteCommand(
            id = ID.new(),
            name = "Command $idx",
            description = "Description $idx",
            icon = null,
            color = ColorsProvider.commandColors[idx % ColorsProvider.commandColors.size].toArgb(),
            durations = mockDurations.mapIndexed { idx, v ->
                if (idx % 2 == 0) v else v * idx
            }.toIntArray()
        )
    }
}
