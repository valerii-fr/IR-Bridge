package dev.nordix.irbridge.remotes.commonUi

import androidx.compose.ui.graphics.toArgb
import dev.nordix.irbridge.ble.model.BlePacketUi
import dev.nordix.irbridge.common_ui.misc.ColorsProvider
import dev.nordix.irbridge.core.utils.ID
import dev.nordix.irbridge.remotes.domain.model.Remote
import dev.nordix.irbridge.remotes.domain.model.RemoteCommand

internal object RemotePreviewDataProvider {
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
            id = ID.Companion.new(),
            name = "Command $idx",
            description = "Description $idx",
            icon = null,
            color = ColorsProvider.commandColors[idx % ColorsProvider.commandColors.size].toArgb(),
            durations = mockDurations.mapIndexed { idx, v ->
                if (idx % 2 == 0) v else v * idx
            }.toIntArray()
        )
    }

    val mockRemotes = List(4) { idx ->
        Remote(
            id = ID.Companion.new(),
            name = "Remote $idx",
            description = "Description $idx",
            commands = mockCommands
        )
    }
}