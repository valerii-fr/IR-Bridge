package dev.nordix.irbridge.remotes.screens.list.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.RemoteAddScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.nordix.irbridge.common_ui.misc.AddFab
import dev.nordix.irbridge.export.domain.rememberBackupCallbacks
import dev.nordix.irbridge.remotes.R
import dev.nordix.irbridge.remotes.nav.RemotesGraph
import dev.nordix.irbridge.remotes.screens.add.ui.RemoteAddArgs
import dev.nordix.irbridge.remotes.screens.list.model.RemotesListScreenViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Destination<RemotesGraph>(start = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemotesListScreen(
    navigator: DestinationsNavigator,
    snackbarHost: SnackbarHostState,
) {
    val viewModel = koinViewModel<RemotesListScreenViewModel>()
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showExportMenu by remember { mutableStateOf(false) }


    val backupCallbacks = rememberBackupCallbacks(
        onError = {
            coroutineScope.launch {
                snackbarHost.showSnackbar(it.message ?: "Unknown error")
            }
        },
        onDone = {
            coroutineScope.launch {
                snackbarHost.showSnackbar(it)
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddFab(onClick = { navigator.navigate(RemoteAddScreenDestination()) })
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.remotes)) },
                actions = {
                    IconButton(
                        onClick = { showExportMenu = !showExportMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = showExportMenu,
                        onDismissRequest = { showExportMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.export)) },
                            onClick = {
                                showExportMenu = false
                                backupCallbacks.onExportClick()
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Upload,
                                    contentDescription = null
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.import_db)) },
                            onClick = {
                                showExportMenu = false
                                backupCallbacks.onImportClick()
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null
                                    )
                            }
                        )
                    }
                }
            )
        },
    ) { pv ->
        RemotesListLayout(
            modifier = Modifier.padding(pv),
            state = state,
            onEvent = viewModel::onEvent,
            onEdit = {
                navigator.navigate(RemoteAddScreenDestination(RemoteAddArgs(it)))
            }
        )
    }
}
