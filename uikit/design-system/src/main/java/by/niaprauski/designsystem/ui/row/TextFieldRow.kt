package by.niaprauski.designsystem.ui.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.text.TextMedium
import by.niaprauski.designsystem.ui.texxtfield.CTextField

@Composable
fun TextFieldRow(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextMedium(text = label)

        CTextField(
            modifier = Modifier.width(AppTheme.viewSize.short_text_field),
            value = text,
            isError = isError,
            onValueChange = { text -> onValueChange(text) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }
}