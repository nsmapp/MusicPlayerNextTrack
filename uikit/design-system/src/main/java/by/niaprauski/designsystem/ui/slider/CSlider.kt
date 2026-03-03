package by.niaprauski.designsystem.ui.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import by.niaprauski.designsystem.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSlider(
    modifier: Modifier = Modifier,
    trackProgress: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit,
) {
    Slider(
        value = trackProgress,
        onValueChange = { newPosition -> onValueChange(newPosition) },
        modifier = modifier,
        valueRange = valueRange,
        steps = steps,
        thumb = {
            Spacer(
                modifier = Modifier
                    .padding(vertical = AppTheme.padding.mini)
                    .size(AppTheme.viewSize.micro)
                    .clip(RoundedCornerShape(AppTheme.viewSize.micro))
                    .background(AppTheme.appColors.text)
                    .padding(vertical = AppTheme.viewSize.border_small)
                    .background(AppTheme.appColors.accent)
            )
        },
        track = {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.viewSize.border_big)
                    .background(
                        color = AppTheme.appColors.accent,
                        shape = RoundedCornerShape(AppTheme.radius.micro)
                    )
            )
        }
    )
}