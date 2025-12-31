package dev.nordix.irbridge.common_ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.nordix.irbridge.common_ui.card.common.RDCardItem
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.common_ui.theme.spacers

@Composable
fun RDCheckboxListItem(
    modifier: Modifier = Modifier,
    item: RDCardItem.RDClickableContent.Checkbox,
) {
    RDListItem(
        modifier = modifier,
        content = {
            Text(text = item.text)
        },
        trailingContent = {
            Checkbox(
                checked = item.checked,
                onCheckedChange = item.onCheckedChange,
                enabled = item.enabled
            )
        },
    )
}

@Composable
fun RDSwitchListItem(
    modifier: Modifier = Modifier,
    item: RDCardItem.RDClickableContent.Switch,
) {
    RDListItem(
        modifier = modifier,
        content = {
            Text(text = item.text)
        },
        trailingContent = {
            Switch(
                checked = item.checked,
                onCheckedChange = item.onCheckedChange,
                enabled = item.enabled
            )
        },
    )
}

@Composable
fun RDOptionListItem(
    modifier: Modifier = Modifier,
    item: RDCardItem.RDClickableContent.Option,
) {
    RDListItem(
        modifier = modifier.clickable(
            enabled = item.enabled,
            onClick = item.onClick
        ),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = MaterialTheme.paddings.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.text,
                )
                item.value?.let { value ->
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        leadingContent = item.iconResId?.let {
            {
                Icon(
                    painter = it,
                    contentDescription = null
                )
            }
        },
        trailingContent = if (item.enabled) {
            {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        } else null,
    )
}

@Composable
fun RDNavigationListItem(
    modifier: Modifier = Modifier,
    item: RDCardItem.RDClickableContent.Navigation,
) {
    RDListItem(
        modifier = modifier.clickable(
            enabled = item.enabled,
            onClick = item.onClick,
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null
        ),
        content = {
            Text(text = item.text)
        },
        leadingContent = item.iconResId?.let {
            {
                Icon(
                    painter = it,
                    contentDescription = null
                )
            }
        },
        trailingContent = if (item.enabled) {
            {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        } else null,
    )
}

@Composable
fun RDButtonListItem(
    modifier: Modifier = Modifier,
    item: RDCardItem.RDClickableContent.Button,
) {
    RDListItem(
        modifier = modifier,
        content = {
            Column {
                item.text?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                item.subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
        trailingContent = {
            Button(
                onClick = item.onClick,
                enabled = item.enabled,
            ) {
                Text(text = item.buttonText)
            }
        },
    )
}

@Composable
internal fun RDListItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.small)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium
            ) {
                leadingContent?.invoke()
                content()
            }
        }
        trailingContent?.invoke()
    }
}
