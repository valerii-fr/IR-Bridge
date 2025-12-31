package dev.nordix.irbridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.nordix.irbridge.ble.data.PermissionsHelper
import dev.nordix.irbridge.common_ui.card.common.RDCardView
import dev.nordix.irbridge.common_ui.theme.IRTheme
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.feature.widget.domain.WidgetUpdateBridge
import dev.nordix.irbridge.ir.domain.IrTransmitter
import dev.nordix.irbridge.remotes.nav.RemotesScreenNav
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val permissionHelper: PermissionsHelper by inject()
    private val transmitter: IrTransmitter by inject()

    init {
        permissionHelper.registerLaunchers(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IRTheme {
                if (transmitter.hasIrTransmitter()) {
                    RemotesScreenNav(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(MaterialTheme.paddings.medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        RDCardView {
                            title(stringResource(R.string.no_IR_title))
                            subtitle(stringResource(R.string.no_IR_sub))
                            button(
                                text = stringResource(R.string.stop_app),
                                subtitle = stringResource(R.string.stop_app_insist),
                                buttonText = stringResource(R.string.stop_app_terminate),
                                onClick = {
                                    finish()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionHelper.checkBlePermissionsIfNeeded()
        WidgetUpdateBridge.requestUpdate(this)
    }

    override fun onStop() {
        WidgetUpdateBridge.requestUpdate(this)
        super.onStop()
    }

}
