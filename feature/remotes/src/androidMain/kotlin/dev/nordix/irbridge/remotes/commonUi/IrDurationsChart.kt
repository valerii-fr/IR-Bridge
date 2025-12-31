package dev.nordix.irbridge.remotes.commonUi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Визуализирует IR durations (microseconds): [on0, off0, on1, off1, ...]
 * Рисует "цифровой" сигнал: высокий уровень на ON, низкий на OFF.
 */
@Composable
fun IrDurationsChart(
    durationsUs: IntArray,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 1.dp,
    backgroundColor: Color = Color.Transparent,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    maxWidth: Dp? = null,
) {
    val safe = durationsUs.filter { it > 0 }.toIntArray()
    val totalUs = safe.sum().coerceAtLeast(1)

    val strokePx = strokeWidth

    Box(
        modifier = modifier
            .then(if (backgroundColor != Color.Transparent) Modifier.background(backgroundColor) else Modifier)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (safe.isEmpty()) return@Canvas

            val w = size.width
            val h = size.height
            if (w <= 0f || h <= 0f) return@Canvas

            val topY = h * 0.25f
            val bottomY = h * 0.75f

            val virtualW = maxWidth?.toPx() ?: w
            val xScale = w / max(virtualW, 1f)

            fun xForTimeUs(tUs: Int): Float {
                val xVirtual = (tUs.toFloat() / totalUs.toFloat()) * virtualW
                return xVirtual * xScale
            }

            val path = Path()
            var t = 0
            var isHigh = true

            path.moveTo(0f, topY)

            for (i in safe.indices) {
                val d = safe[i]
                val xNext = xForTimeUs(t + d)
                path.lineTo(xNext, if (isHigh) topY else bottomY)
                t += d
                isHigh = !isHigh
                if (i != safe.lastIndex) {
                    path.lineTo(xNext, if (isHigh) topY else bottomY)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = strokePx.toPx())
            )
        }
    }
}
