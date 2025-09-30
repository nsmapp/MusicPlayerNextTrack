package by.niaprauski.designsystem.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import by.niaprauski.designsystem.theme.AppTheme

@Composable
fun TextMediumMicro(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = AppTheme.appColors.text,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = AppTheme.typography.medium_micro
) {
    TextBase(
        modifier = modifier,
        text = text,
        color = color,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        style = style
    )
}

@Composable
fun TextMediumMicro(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    color: Color = AppTheme.appColors.text,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = AppTheme.typography.medium_micro
) {
    TextBase(
        modifier = modifier,
        text = text,
        color = color,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        style = style
    )
}