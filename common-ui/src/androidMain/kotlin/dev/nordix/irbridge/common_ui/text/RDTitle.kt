package dev.nordix.irbridge.common_ui.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

@Composable
fun RDTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = LocalTextStyle.current.color
    )
}

@Composable
fun RDSubtitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = LocalTextStyle.current.color
    )
}

@Composable
fun RDTextBlock(
    modifier: Modifier = Modifier,
    text: String,
    fontFamily: FontFamily = FontFamily.Default,
    style: TextStyle = TextStyle.Default,
    supportingText: String?,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = fontFamily,
            )
            supportingText?.let { text ->
                Text(
                    text = text,
                    style = style,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = fontFamily,
                )
            }
        }
        trailingContent?.invoke()
    }
}
