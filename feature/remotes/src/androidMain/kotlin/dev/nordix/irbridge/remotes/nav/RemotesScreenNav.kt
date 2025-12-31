package dev.nordix.irbridge.remotes.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.destinations.RemoteAddScreenDestination
import com.ramcosta.composedestinations.generated.destinations.RemotesListScreenDestination
import com.ramcosta.composedestinations.generated.navgraphs.RemotesNavGraph
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dev.nordix.irbridge.remotes.screens.add.ui.RemoteAddScreen
import dev.nordix.irbridge.remotes.screens.list.ui.RemotesListScreen

@Composable
fun RemotesScreenNav(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { pv ->
        DestinationsNavHost(
            navController = navController,
            navGraph = RemotesNavGraph,
            modifier = modifier.padding(pv).fillMaxSize(),
        ) {
            composable(
                destination = RemoteAddScreenDestination
            ) {
                RemoteAddScreen(
                    args = navArgs,
                    navigator = destinationsNavigator
                )
            }
            composable(
                destination = RemotesListScreenDestination
            ) {
                RemotesListScreen(
                    navigator = destinationsNavigator
                )
            }
        }
    }
}
