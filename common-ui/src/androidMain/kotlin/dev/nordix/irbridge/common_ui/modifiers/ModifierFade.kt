package dev.nordix.irbridge.common_ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

private enum class FadePosition(val shift: Int) {
    Start(1 shl 0),
    Center(1 shl 2),
    End(1 shl 3),
    Top(1 shl 1),
    Bottom(1 shl 4),
    Combined(0),
}

/**
 * Represents a position of fade. Use with 'and' operator for multiple positioning.
 * For example Fade.Start and Fade.End in [Modifier.fade] will produce
 * two vertical fades at the start and the end of composable.
 * Passing 'Fade.Start and Fade.End and Fade.Top' will invoke previous ones
 * and one more horizontal fade at the top.
 * It can also be used with chain call,
 *
 * e.g. Modifier.fade(Fade.Start, 16.dp).fade(Fade.End, 16.dp)
 */
sealed class Fade(private val position: FadePosition) {
    open val shift: Int get() = position.shift

    infix fun and(other: Fade): Fade {
        return Combined(this.shift or other.shift)
    }

    private class Combined(param: Int) : Fade(FadePosition.Combined) {
        override val shift = param
    }

    object Start : Fade(FadePosition.Start)
    object Top : Fade(FadePosition.Top)
    object End : Fade(FadePosition.End)
    object Bottom : Fade(FadePosition.Bottom)
    object Center : Fade(FadePosition.Center)
}

@Suppress("LongMethod")
fun Modifier.fade(
    fade: Fade,
    width: Dp,
    color: Color = Color.Red,
    compositingStrategy: CompositingStrategy = CompositingStrategy.Offscreen
): Modifier {
    return graphicsLayer(compositingStrategy = compositingStrategy)
        .drawWithContent {
            drawContent()

            val activePositions = FadePosition.values().filter { fade.shift and it.shift > 0 }

            val brushes = activePositions.mapNotNull { position ->
                when (position) {
                    FadePosition.Start -> {
                        val endX = width.toPx()
                        linearGradient(
                            0f to color,
                            1f to Color.Transparent,
                            start = Offset(0f, 0f),
                            end = Offset(endX, 0f)
                        )
                    }

                    FadePosition.End -> {
                        val startX = size.width
                        val endX = size.width - width.toPx()
                        linearGradient(
                            0f to color,
                            1f to Color.Transparent,
                            start = Offset(startX, 0f),
                            end = Offset(endX, 0f)
                        )
                    }

                    FadePosition.Top -> {
                        val endY = width.toPx()
                        linearGradient(
                            0f to color,
                            1f to Color.Transparent,
                            start = Offset(0f, 0f),
                            end = Offset(0f, endY)
                        )
                    }

                    FadePosition.Bottom -> {
                        val startY = size.height
                        val endY = size.height - width.toPx()
                        linearGradient(
                            0f to color,
                            1f to Color.Transparent,
                            start = Offset(0f, startY),
                            end = Offset(0f, endY)
                        )
                    }

                    FadePosition.Center -> {

                        radialGradient(
                            0f to color,
                            1f to Color.Transparent,
                            center = Offset(size.width / 2, size.height / 2),
                            radius = width.toPx()
                        )
                    }

                    FadePosition.Combined -> null
                }
            }
            brushes.forEach { brush ->
                drawRect(
                    brush = brush,
                    blendMode = BlendMode.DstOut
                )
            }
        }
}
