package dev.nordix.irbridge.feature.widget

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.nordix.irbridge.common_ui.theme.IRTheme
import dev.nordix.irbridge.feature.widget.domain.RemoteDialogContract
import dev.nordix.irbridge.feature.widget.ui.RemoteDialogScreen

class WidgetControlActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("WidgetControlActivity", "onCreate: ${intent?.action} ${intent?.extras}")

        // 1) Валидация входного интента
        val isValidAction = intent?.action == RemoteDialogContract.ACTION_OPEN_REMOTE_DIALOG
        val remoteId = intent?.getStringExtra(RemoteDialogContract.EXTRA_REMOTE_ID)
        val fromWidget = intent?.getBooleanExtra(RemoteDialogContract.EXTRA_FROM_WIDGET, false) ?: false

        if (!isValidAction || remoteId.isNullOrBlank()) {
            finish()
            return
        }

        enableEdgeToEdge()

        setContent {
            IRTheme {
                RemoteDialogScreen(
                    remoteId = remoteId,
                    fromWidget = fromWidget,
                    onDismiss = { finish() }
                )
            }
        }
    }

}
