package dev.nordix.irbridge.common_ui.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import dev.nordix.irbridge.common_ui.theme.IRTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import dev.nordix.irbridge.common_ui.theme.spacers

@Composable
fun RDOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    canMaskPassword: Boolean = false,
    showClearButton: Boolean = false,
) {

    var showPassword by remember { mutableStateOf(!canMaskPassword) }
    val visualTransformation = if (showPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    val trailingIconView: @Composable (() -> Unit)? =
        if (showClearButton || canMaskPassword) {{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.small)
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
                        onClick = { onValueChange("") },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                }
                trailingIcon?.invoke()
            }
        }} else {
            trailingIcon?.let { { it.invoke() } }
        }


    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = label?.let { text ->
            { Text(text) }
        },
        placeholder = placeholder?.let { text ->
            { Text(text) }
        },
        enabled = enabled,
        textStyle = textStyle,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIconView,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
@Preview
private fun RDOutlinedTextFieldPreview() {
    IRTheme {
        var text by remember { mutableStateOf("") }
        RDOutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "Password",
            label = "Label",
            showClearButton = true,
            canMaskPassword = true,
        )
    }
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
