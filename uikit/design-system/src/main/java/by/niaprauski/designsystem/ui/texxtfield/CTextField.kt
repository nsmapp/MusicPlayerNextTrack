package by.niaprauski.designsystem.ui.texxtfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import by.niaprauski.designsystem.icons.SmallIcon
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.text.TextMedium

@Composable
fun CTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AppTheme.typography.medium,
    hint: String? = null,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit = {},
    trailingIcon: ImageVector? = null,
    onTrailingClick: () -> Unit = {},
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(AppTheme.radius.default),
    colors: TextFieldColors = TextFieldDefaults.colors()
        .copy(
            focusedTextColor = AppTheme.colors.text,
            unfocusedTextColor = AppTheme.colors.text,
            disabledTextColor = AppTheme.colors.text_ligth,
            focusedContainerColor = AppTheme.colors.background_hard,
            unfocusedContainerColor = AppTheme.colors.background,
            disabledContainerColor = AppTheme.colors.background,
            cursorColor = AppTheme.colors.accent,
            textSelectionColors = TextSelectionColors(
                handleColor = AppTheme.colors.accent,
                backgroundColor = AppTheme.colors.foreground
            ),
            focusedPlaceholderColor = AppTheme.colors.text_ligth,
            focusedIndicatorColor = AppTheme.colors.transparent,
            unfocusedIndicatorColor = AppTheme.colors.transparent

        )
) {

    TextField(
        modifier = modifier.border(
            border = BorderStroke(
                width = AppTheme.viewSize.border_normal,
                color = AppTheme.colors.accent
            ),
            shape = shape
        ),
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = {
            hint?.let {
                TextMedium(
                    text = hint,
                    color = colors.focusedPlaceholderColor
                )
            }
        },
        leadingIcon = {
            leadingIcon?.let { icon ->
                SmallIcon(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onLeadingClick() }),
                    imageVector = icon,
                    colorFilter = ColorFilter.tint(AppTheme.colors.text_ligth)
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let { icon ->
                SmallIcon(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTrailingClick() }),
                    imageVector = icon,
                    colorFilter = ColorFilter.tint(AppTheme.colors.text)
                )
            }
        },
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = shape,
        colors = colors,
    )

}


@Preview
@Composable
fun PreviewTextField() {
    AppTheme {
        CTextField(
            value = "",
            onValueChange = {},
            hint = "hint text",
            leadingIcon = Icons.Default.Search,
            trailingIcon = Icons.Default.Settings,
        )
    }
}
