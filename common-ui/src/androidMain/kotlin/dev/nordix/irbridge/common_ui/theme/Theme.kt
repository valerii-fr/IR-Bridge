package dev.nordix.irbridge.common_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import ru.rd_inspector.ui.theme.model.Paddings
import ru.rd_inspector.ui.theme.model.Spacers

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    error = Error,
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    error = Error,
    onPrimary = DarkOnPrimary,
    surfaceContainer = DarkSurfaceContainer,
)

@Suppress("UnusedReceiverParameter")
val MaterialTheme.paddings: Paddings
    @Composable
    @ReadOnlyComposable
    get() = LocalPaddings.current


@Suppress("UnusedReceiverParameter")
val MaterialTheme.spacers: Spacers
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacers.current


private val LocalPaddings = staticCompositionLocalOf<Paddings> {
    error("CompositionLocal LocalPaddings is not present")
}

private val LocalSpacers = staticCompositionLocalOf<Spacers> {
    error("CompositionLocal LocalSpacers is not present")
}
@Composable
fun IRTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) {
        DarkColors
    } else {
        LightColors
    }
    MaterialTheme(
        colorScheme = colors,
    ) {
        CompositionLocalProvider(
            LocalPaddings provides Paddings,
            LocalSpacers provides Spacers,
        ) {
            content()
        }
    }
}
