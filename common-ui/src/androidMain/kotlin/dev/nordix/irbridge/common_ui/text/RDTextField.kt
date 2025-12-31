package dev.nordix.irbridge.common_ui.text

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.nordix.irbridge.common_ui.theme.IRTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.common_ui.theme.spacers

@Composable
fun RDTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    error: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: String? = null,
    canMaskPassword: Boolean = false,
    showClearButton: Boolean = false,
) {
    var showPassword by remember { mutableStateOf(!canMaskPassword) }
    val visualTransformation = if (showPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
    Surface(
        modifier = modifier
            .defaultMinSize(
                minWidth = RDOutlinedTextFieldDefaults.MinWidth,
                minHeight = RDOutlinedTextFieldDefaults.MinHeight
            ),
        shape = RoundedCornerShape(RDOutlinedTextFieldDefaults.CORNER_RADIUS_PERCENT),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(RDOutlinedTextFieldDefaults.Elevation),
        border = if (error) {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            null
        }
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = textStyle,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.small)
                    ) {
                        leadingIcon?.invoke() ?: run {
                            Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
                        }
                        Box {
                            innerTextField()
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder ?: "",
                                    style = textStyle.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.padding(end = MaterialTheme.paddings.small),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (canMaskPassword) {
                            IconButton(
                                onClick = { showPassword = !showPassword }
                            ) {
                                Icon(
                                    imageVector = if (showPassword) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = null,
                                )
                            }
                        }
                        if (showClearButton) {
                            IconButton(
                                onClick = { onValueChange("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                )
                            }
                        }
                        trailingIcon?.invoke()
                    }
                }
            }
        )
    }
}

private object RDOutlinedTextFieldDefaults {
    val Elevation = 1.dp
    const val CORNER_RADIUS_PERCENT: Int = 50
    val MinHeight = 56.dp
    val MinWidth = 160.dp
}

@Composable
@Preview
private fun RDTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    IRTheme {
        RDTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "Password",
            showClearButton = true,
            canMaskPassword = true,
        )
    }
}
