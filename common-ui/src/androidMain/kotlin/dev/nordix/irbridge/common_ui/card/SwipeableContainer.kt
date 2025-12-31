package dev.nordix.irbridge.common_ui.card

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import dev.nordix.irbridge.common_ui.theme.IRTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import dev.nordix.irbridge.common_ui.theme.paddings
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun SwipeableContainer(
    modifier:       Modifier = Modifier,
    swipeEnabled:   Boolean = true,
    key:            Any? = null,
    actionsStart:   @Composable RowScope.() -> Unit = {},
    actionsEnd:     @Composable RowScope.() -> Unit = {},
    content:        @Composable () -> Unit,
) {
    key(key) {
        val state = remember {
            AnchoredDraggableState(
                initialValue = DragAnchors.Mid,
            )
        }

        SubcomposeLayout(
            modifier = modifier.anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal,
                enabled = swipeEnabled,
            )
        ) { rawConstraints ->
            val loose = Constraints(
                minWidth = 0, maxWidth = Constraints.Infinity,
                minHeight = 0, maxHeight = rawConstraints.maxHeight
            )

            val startPlaceables = subcompose("startActions") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = actionsStart
                )
            }.map { it.measure(loose) }

            val startWidth = startPlaceables.maxOfOrNull { it.width } ?: 0
            val startHeight = startPlaceables.maxOfOrNull { it.height } ?: 0

            val endPlaceables = subcompose("endActions") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = actionsEnd
                )
            }.map { it.measure(loose) }

            val endWidth = endPlaceables.maxOfOrNull { it.width } ?: 0
            val endHeight = endPlaceables.maxOfOrNull { it.height } ?: 0

            val contentPlaceables = subcompose("content") {
                Box(contentAlignment = Alignment.Center) { content() }
            }.map { it.measure(rawConstraints) }

            val contentWidth = contentPlaceables.maxOfOrNull { it.width } ?: 0
            val contentHeight = contentPlaceables.maxOfOrNull { it.height } ?: 0

            val width = max(contentWidth, max(startWidth, endWidth)).coerceIn(
                rawConstraints.minWidth, rawConstraints.maxWidth
            )
            val height = max(contentHeight, max(startHeight, endHeight)).coerceIn(
                rawConstraints.minHeight, rawConstraints.maxHeight
            )

            state.updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at startWidth.toFloat()
                    DragAnchors.Mid at 0f
                    DragAnchors.End at -endWidth.toFloat()
                }
            )

            layout(width, height) {
                startPlaceables.forEach { p ->
                    p.placeRelative(x = 0, y = (height - p.height) / 2)
                }

                endPlaceables.forEach { p ->
                    p.placeRelative(x = width - p.width, y = (height - p.height) / 2)
                }

                val offsetX = state.requireOffset().roundToInt()
                contentPlaceables.forEach { p ->
                    p.placeRelative(x = offsetX, y = (height - p.height) / 2)
                }
            }
        }
    }
}

@Composable
@Preview
private fun SwipeableContainerPreview() {
    IRTheme {
        Surface(
            modifier = Modifier.padding(MaterialTheme.paddings.extraLarge)
        ) {
            SwipeableContainer(
                modifier = Modifier.fillMaxWidth(),
                actionsStart = {
                    Text("AS")
                },
                actionsEnd = {
                    Text("AE")
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Test content")
                    }
                }
            }
        }
    }
}

enum class DragAnchors {
    Start,
    Mid,
    End,
}
