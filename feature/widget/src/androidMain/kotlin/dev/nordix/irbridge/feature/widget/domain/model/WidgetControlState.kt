package dev.nordix.irbridge.feature.widget.domain.model

import dev.nordix.irbridge.remotes.domain.model.Remote

data class WidgetControlState(
    val remote: Remote? = null,
)
