package by.niaprauski.designsystem.ui.check

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.niaprauski.designsystem.theme.AppTheme


@Composable
fun CSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
) {

    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppTheme.appColors.foreground,
            checkedTrackColor = AppTheme.appColors.accent,
            checkedBorderColor = AppTheme.appColors.foreground,
            uncheckedThumbColor = AppTheme.appColors.foreground,
            uncheckedTrackColor = AppTheme.appColors.accent,
            uncheckedIconColor = AppTheme.appColors.foreground
        )
    )
}