package by.niaprauski.designsystem.ui.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.niaprauski.designsystem.ui.check.CSwitch
import by.niaprauski.designsystem.ui.text.TextMedium

@Composable
fun SwitchRow(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    label: String,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextMedium(text = label)

        CSwitch(
            checked = isChecked,
            onCheckedChange = { isChecked -> onCheckedChange(isChecked) }
        )
    }
}