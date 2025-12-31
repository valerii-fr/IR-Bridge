package dev.nordix.irbridge.remotes.screens.list.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.RemoteAddScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.nordix.irbridge.common_ui.misc.AddFab
import dev.nordix.irbridge.remotes.R
import dev.nordix.irbridge.remotes.nav.RemotesGraph
import dev.nordix.irbridge.remotes.screens.add.ui.RemoteAddArgs
import dev.nordix.irbridge.remotes.screens.list.model.RemotesListScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@Destination<RemotesGraph>(start = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemotesListScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = koinViewModel<RemotesListScreenViewModel>()
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddFab(onClick = { navigator.navigate(RemoteAddScreenDestination()) })
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.remotes)) },
            )
        }
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
