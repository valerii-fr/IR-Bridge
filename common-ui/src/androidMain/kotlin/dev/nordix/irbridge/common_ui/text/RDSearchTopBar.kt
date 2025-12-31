package dev.nordix.irbridge.common_ui.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import dev.nordix.irbridge.common_ui.theme.IRTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RDSearchTopBar(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onSearch: () -> Unit,
) {
    var searchIsActive by remember {
        mutableStateOf(false)
    }

    val focusRequester: FocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    TopAppBar(
        modifier = modifier,
        title = {
            if (searchIsActive) {
                SearchField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = title,
                    focusRequester = focusRequester,
                    onSearch = onSearch
                )
            } else {
                Text(text = title)
            }
        },
        navigationIcon = navigationIcon,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior,
        actions = {
            if (searchIsActive) {
                IconButton(
                    onClick = {
                        onValueChange("")
                        searchIsActive = false
                        focusRequester.freeFocus()
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        searchIsActive = true
                        focusRequester.captureFocus()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                }
            }
        }
    )
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {

    val focusManager = LocalFocusManager.current
    LaunchedEffect(placeholder) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                focusManager.clearFocus()
            }
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
                if (value.isEmpty()) {
                    Text(
                        text = placeholder ?: "",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun RDTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    IRTheme {
        RDSearchTopBar(
            value = text,
            onValueChange = { text = it },
            title = "Add device",
            onSearch = {}
        )
    }
}
