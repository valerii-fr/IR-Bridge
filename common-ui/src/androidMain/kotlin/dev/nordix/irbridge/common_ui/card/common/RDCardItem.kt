package dev.nordix.irbridge.common_ui.card.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

sealed interface RDCardItem {

    data class Raw(val block: @Composable () -> Unit) : RDCardItem

    sealed interface RDHeaderContent : RDCardItem {

        val text: String
        val style: TextStyle
        val fontFamily: FontFamily
        data class Title(
            override val text: String,
            override val fontFamily: FontFamily = FontFamily.Default,
            override val style: TextStyle = TextStyle.Default
        ) : RDHeaderContent
        data class Subtitle(
            override val text: String,
            override val fontFamily: FontFamily = FontFamily.Default,
            override val style: TextStyle = TextStyle.Default
        ) : RDHeaderContent
    }

    sealed interface RDTextContent : RDCardItem {


        val text: String
        val style: TextStyle
        val fontFamily: FontFamily

        data class Text(
            override val text: String,
            override val fontFamily: FontFamily = FontFamily.Default,
            override val style: TextStyle = TextStyle.Default
        ) : RDTextContent
        data class TextValue(
            override val text: String,
            override val fontFamily: FontFamily = FontFamily.Default,
            override val style: TextStyle = TextStyle.Default,
            val label: String
        ) : RDTextContent
        data class TextBlock(
            override val text: String,
            override val fontFamily: FontFamily = FontFamily.Default,
            override val style: TextStyle = TextStyle.Default,
            val supportingText: String?,
            val trailingContent: (@Composable () -> Unit)? = null
        ) : RDTextContent
    }

    sealed interface RDClickableContent : RDCardItem {
        data class Checkbox(
            val text: String,
            val checked: Boolean,
            val enabled: Boolean = true,
            val onCheckedChange: (Boolean) -> Unit
        ) : RDClickableContent

        data class Switch(
            val text: String,
            val checked: Boolean,
            val enabled: Boolean = true,
            val onCheckedChange: (Boolean) -> Unit
        ) : RDClickableContent

        data class Navigation(
            val text: String,
            val iconResId: Painter?,
            val enabled: Boolean = true,
            val onClick: () -> Unit
        ) : RDClickableContent

        data class Option(
            val text: String,
            val value: String?,
            val iconResId: Painter?,
            val enabled: Boolean = true,
            val onClick: () -> Unit
        ) : RDClickableContent

        data class Button(
            val text: String? = null,
            val subtitle: String? = null,
            val buttonText: String,
            val enabled: Boolean = true,
            val onClick: () -> Unit
        ) : RDClickableContent
    }

    sealed interface RDMenuContent : RDCardItem {
        data class Dropdown(
            val text: String,
            val enabled: Boolean = true,
            val items: List<String>,
            val selectedItem: String,
            val onItemSelected: (String) -> Unit
        ) : RDMenuContent

        data class Carousel<T>(
            val text: String? = null,
            val enabled: Boolean = true,
            val items: List<T>,
            val selectedItem: Int,
            val onPrev: () -> Unit,
            val onNext: () -> Unit,
            val onClick: (() -> Unit)? = null,
            val itemToString: @Composable (T) -> String = { it.toString() },
        ) : RDMenuContent
    }

}
