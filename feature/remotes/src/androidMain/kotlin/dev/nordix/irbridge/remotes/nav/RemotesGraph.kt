package dev.nordix.irbridge.remotes.nav

import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.parameters.CodeGenVisibility

@NavHostGraph(
    route = "remotes",
    visibility = CodeGenVisibility.INTERNAL,
)
annotation class RemotesGraph
