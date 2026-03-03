package by.niaprauski.designsystem.ui.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun <T> TwoActionIconButton(
    modifier: Modifier = Modifier,
    model: T,
    isFirstAction: Boolean,
    onActionFirstClick: (T) -> Unit,
    onActionSecondClick: (T) -> Unit,
    iconFirst: ImageVector,
    iconSecond: ImageVector,
    descriptionFirst: String,
    descriptionSecond: String,
    label: String = "TwoActionIconButton",
) {

    val currentOnFirstClick by rememberUpdatedState(onActionFirstClick)
    val currentOnSecondClick by rememberUpdatedState(onActionSecondClick)
    val currentModel by rememberUpdatedState(model)

    val handleFirstClick = remember { { currentOnFirstClick(currentModel) } }
    val handleSecondClick = remember { { currentOnSecondClick(currentModel) } }

    Crossfade(
        modifier = modifier,
        targetState = isFirstAction,
        label = label,
    ) { isFirst ->
        if (isFirst) {
            PlayerLiteButton(
                modifier = Modifier.fillMaxSize(),
                imageVector = iconFirst,
                onClick = handleFirstClick,
                description = descriptionFirst,
            )
        } else {
            PlayerLiteButton(
                modifier = Modifier.fillMaxSize(),
                imageVector = iconSecond,
                onClick = handleSecondClick,
                description = descriptionSecond,
            )
        }
    }
}