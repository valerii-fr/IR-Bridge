package dev.nordix.irbridge.common_ui.card.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import dev.nordix.irbridge.common_ui.list.RDButtonListItem
import dev.nordix.irbridge.common_ui.list.RDCheckboxListItem
import dev.nordix.irbridge.common_ui.list.RDNavigationListItem
import dev.nordix.irbridge.common_ui.list.RDOptionListItem
import dev.nordix.irbridge.common_ui.list.RDSwitchListItem
import dev.nordix.irbridge.common_ui.menu.RDCarouselMenu
import dev.nordix.irbridge.common_ui.menu.RDDropdownMenu
import dev.nordix.irbridge.common_ui.text.RDSubtitle
import dev.nordix.irbridge.common_ui.text.RDTextBlock
import dev.nordix.irbridge.common_ui.text.RDTitle
import dev.nordix.irbridge.common_ui.theme.IRTheme
import dev.nordix.irbridge.common_ui.theme.paddings
import dev.nordix.irbridge.common_ui.theme.spacers

data class RDCardContent(val id: String? = null, val items: List<RDCardItem>)

@DslMarker
annotation class RDCardDsl

@RDCardDsl
@Suppress("unused", "TooManyFunctions")
class RDCardBuilder {
    private val items = mutableListOf<RDCardItem>()

    // --- Header / Text ---
    fun title(
        text: String,
        style: TextStyle = TextStyle.Default,
        fontFamily: FontFamily = FontFamily.Default
    ) {
        items += RDCardItem.RDHeaderContent.Title(text, fontFamily, style)
    }

    fun subtitle(
        text: String,
        style: TextStyle = TextStyle.Default,
        fontFamily: FontFamily = FontFamily.Default
    ) {
        items += RDCardItem.RDHeaderContent.Subtitle(text, fontFamily, style)
    }

    fun text(
        text: String,
        style: TextStyle = TextStyle.Default,
        fontFamily: FontFamily = FontFamily.Default
    ) {
        items += RDCardItem.RDTextContent.Text(text, fontFamily, style)
    }

    fun textValue(
        label: String,
        text: String,
        style: TextStyle = TextStyle.Default,
        fontFamily: FontFamily = FontFamily.Default
    ) {
        items += RDCardItem.RDTextContent.TextValue(label, fontFamily, style, text)
    }

    fun textBlock(
        text: String,
        supportingText: String?,
        style: TextStyle = TextStyle.Default,
        fontFamily: FontFamily = FontFamily.Default,
        trailingContent: (@Composable () -> Unit)? = null
    ) {
        items += RDCardItem.RDTextContent.TextBlock(text, fontFamily, style, supportingText, trailingContent)
    }

    fun raw(
        block: @Composable () -> Unit
    ) {
        items += RDCardItem.Raw(block)
    }

    // --- Clickable ---
    fun checkbox(
        text: String,
        checked: Boolean = false,
        enabled: Boolean = true,
        onCheckedChange: (Boolean) -> Unit = {}
    ) {
        items += RDCardItem.RDClickableContent.Checkbox(text, checked, enabled, onCheckedChange)
    }

    fun switch(
        text: String,
        checked: Boolean = false,
        enabled: Boolean = true,
        onCheckedChange: (Boolean) -> Unit = {}
    ) {
        items += RDCardItem.RDClickableContent.Switch(text, checked, enabled, onCheckedChange)
    }

    fun navigation(
        text: String,
        icon: Painter? = null,
        enabled: Boolean = true,
        onClick: () -> Unit = {}
    ) {
        items += RDCardItem.RDClickableContent.Navigation(text, icon, enabled, onClick)
    }

    fun option(
        text: String,
        value: String?,
        icon: Painter? = null,
        enabled: Boolean = true,
        onClick: () -> Unit = {}
    ) {
        items += RDCardItem.RDClickableContent.Option(text, value, icon, enabled, onClick)
    }

    fun button(
        text: String,
        subtitle: String? = null,
        buttonText: String,
        enabled: Boolean = true,
        onClick: () -> Unit = {},
    ) {
        items += RDCardItem.RDClickableContent.Button(text, subtitle, buttonText, enabled, onClick)
    }

    // --- Menus ---
    fun dropdown(
        text: String,
        itemsList: List<String>,
        selectedItem: String,
        enabled: Boolean = true,
        onItemSelected: (String) -> Unit = {}
    ) {
        items += RDCardItem.RDMenuContent.Dropdown(text, enabled, itemsList, selectedItem, onItemSelected)
    }

    @Composable
    fun <T> carousel(
        text: String? = null,
        enabled: Boolean = true,
        entries: List<T>,
        selectedItem: Int,
        onPrev: () -> Unit,
        onNext: () -> Unit,
        onClick: (() -> Unit)? = null,
        itemToString: @Composable (T) -> String,
    ) {
        items + RDCardItem.RDMenuContent.Carousel(
            text = text,
            enabled = enabled,
            items = entries,
            selectedItem = selectedItem,
            onPrev = onPrev,
            onNext = onNext,
            onClick = onClick,
            itemToString = itemToString,
        ) as RDCardItem.RDMenuContent.Carousel<*>
    }

    operator fun RDCardItem.unaryPlus() { items += this }

    internal fun build(): List<RDCardItem> = items.toList()
}

@RDCardDsl
@Composable
fun rdCard(
    id: String? = null,
    block: @Composable RDCardBuilder.() -> Unit
): RDCardContent {
    val builder = RDCardBuilder()
    builder.block()
    return RDCardContent(id = id, items = builder.build())
}

@RDCardDsl
@Composable
fun RDCardView(
    modifier: Modifier = Modifier,
    id: String? = null,
    key: Any? = null,
    block: @Composable RDCardBuilder.() -> Unit
) {
    val builder = RDCardBuilder()
    builder.block()
    val card = remember(key) {
        RDCardContent(id = id, items = builder.build())
    }

    RDCard(
        modifier = modifier,
        rdCardContent = card
    )
}

@Composable
fun RDCard(
    modifier: Modifier = Modifier,
    rdCardContent: RDCardContent,
) {
    val cardItems = remember(rdCardContent.items) {
        rdCardContent.items.filterNot { it is RDCardItem.RDHeaderContent }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.medium),
        horizontalAlignment = Alignment.Start,
    ) {
        rdCardContent.items
            .filterIsInstance<RDCardItem.RDHeaderContent>()
            .takeIf { it.isNotEmpty() }
            ?.let { items ->
                Column {
                    items.forEach { rdCardItem ->
                        RDHeader(rdCardItem)
                    }
                }
            }
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            elevation = CardDefaults.cardElevation(1.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                cardItems.forEachIndexed { index, rdCardItem ->

                    val cardPaddings = Modifier.composed {
                        if (rdCardItem is RDCardItem.Raw) {
                            Modifier
                        } else {
                            Modifier.padding(MaterialTheme.paddings.medium)
                        }
                    }
                    Column {
                        Box(modifier = cardPaddings) {
                            when (rdCardItem) {
                                is RDCardItem.Raw -> rdCardItem.block()
                                is RDCardItem.RDTextContent -> RDText(rdCardItem)
                                is RDCardItem.RDClickableContent -> RDClickable(rdCardItem)
                                is RDCardItem.RDMenuContent -> RDMenu(rdCardItem)
                                is RDCardItem.RDHeaderContent -> { }
                            }
                        }
                        if (index != cardItems.lastIndex) {
                            HorizontalDivider(
                                thickness = MaterialTheme.spacers.extraSmall,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun RDHeader(item: RDCardItem.RDHeaderContent) {
    when (item) {
        is RDCardItem.RDHeaderContent.Title -> RDTitle(item.text)
        is RDCardItem.RDHeaderContent.Subtitle -> RDSubtitle(item.text)
    }
}

@Composable
internal fun RDText(item: RDCardItem.RDTextContent) {
    when (item) {
        is RDCardItem.RDTextContent.Text -> Text(
            text = item.text,
            style = item.style,
            fontFamily = item.fontFamily,
        )
        is RDCardItem.RDTextContent.TextValue -> Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = item.text,
                style = item.style,
                fontFamily = item.fontFamily,
            )
        }
        is RDCardItem.RDTextContent.TextBlock -> RDTextBlock(
            modifier = Modifier.fillMaxWidth(),
            text = item.text,
            supportingText = item.supportingText,
            trailingContent = item.trailingContent,
            style = item.style,
            fontFamily = item.fontFamily,
        )
    }
}

@Composable
internal fun RDClickable(item: RDCardItem.RDClickableContent) {
    when (item) {
        is RDCardItem.RDClickableContent.Checkbox -> RDCheckboxListItem(item = item)
        is RDCardItem.RDClickableContent.Switch -> RDSwitchListItem(item = item)
        is RDCardItem.RDClickableContent.Navigation -> RDNavigationListItem(item = item)
        is RDCardItem.RDClickableContent.Button -> RDButtonListItem(item = item)
        is RDCardItem.RDClickableContent.Option -> RDOptionListItem(item = item)
    }
}

@Composable
internal fun RDMenu(item: RDCardItem.RDMenuContent) {
    when (item) {
        is RDCardItem.RDMenuContent.Dropdown -> RDDropdownMenu(item = item)
        is RDCardItem.RDMenuContent.Carousel<*> -> RDCarouselMenu(item = item)
    }
}

@Preview
@Composable
private fun RDCardPreview() {
    var c1 by remember { mutableStateOf(false) }
    var c2 by remember { mutableStateOf(false) }
    var c3 by remember { mutableStateOf(false) }
    var s1 by remember { mutableStateOf(false) }

    val card: RDCardContent = rdCard(id = "General") {
        title("Title")
        subtitle("Subtitle")
        text("Plain text in the card")
        textValue("Key", "Value")
        text("Another plain text in the card")
        navigation("Navigation entry")
        button("Button", buttonText = "Button text")
        checkbox("Checkbox 1", checked = c1, onCheckedChange = { c1 = it })
        checkbox("Checkbox 2", checked = c2, onCheckedChange = { c2 = it })
        checkbox("Checkbox 3", checked = c3, onCheckedChange = { c3 = it })
        switch("Switch", checked = s1, onCheckedChange = { s1 = it })
        dropdown("Dropdown", itemsList = listOf("Item 1", "Item 2"), selectedItem = "Item 1")
    }

    IRTheme {
        RDCard(
            modifier = Modifier.fillMaxWidth(),
            rdCardContent = card
        )
    }
}
