package dev.nordix.irbridge.common_ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import dev.nordix.irbridge.common_ui.card.common.RDCardItem
import dev.nordix.irbridge.common_ui.list.RDListItem
import dev.nordix.irbridge.common_ui.text.RDSubtitle
import dev.nordix.irbridge.common_ui.theme.spacers

@Composable
fun RDDropdownMenu(
    modifier: Modifier = Modifier,
    item: RDCardItem.RDMenuContent.Dropdown
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = item.enabled,
                onClick = { expanded = !expanded },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        content = {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                item.items.forEach { itemText ->
                    DropdownMenuItem(
                        text = { Text(text = itemText) },
                        enabled = item.enabled,
                        modifier = Modifier.background(
                            color = if (itemText == item.selectedItem) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                Color.Transparent
                            }
                        ),
                        onClick = {
                            expanded = false
                            item.onItemSelected(itemText)
                        }
                    )
                }
            }
            Text(text = item.selectedItem)
        }
    )
}

@Composable
fun <T> RDCarouselMenu(
    modifier: Modifier = Modifier,
    item: RDCardItem.RDMenuContent.Carousel<T>,
) {
    RDListItem(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item.text?.let { RDSubtitle(it) }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .composed {
                        if (item.onClick != null) {
                            clickable(
                                onClick = item.onClick,
                                enabled = item.enabled,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                        } else {
                            this
                        }
                    }
            ) {
                val contentColor = LocalContentColor.current
                Icon(
                    modifier = Modifier
                        .rotate(ROTATION_180)
                        .clip(CircleShape)
                        .clickable(
                            onClick = item.onPrev,
                            enabled = item.enabled && item.selectedItem > 0,

                        ),
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = if (item.enabled && item.selectedItem > 0) {
                        contentColor
                    } else {
                        contentColor.copy(alpha = 0.5f)
                    }
                )
                Text(text = item.itemToString(item.items[item.selectedItem]))
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(
                            onClick = item.onNext,
                            enabled = item.enabled && item.selectedItem < item.items.lastIndex,
                        ),
                   imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = if (item.enabled && item.selectedItem < item.items.lastIndex) {
                        contentColor
                    } else {
                        contentColor.copy(alpha = 0.5f)
                    }
                )
            }
        }
    }
}

private const val ROTATION_180 = 180f
