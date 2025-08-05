package by.niaprauski.designsystem.ui.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import by.niaprauski.designsystem.theme.AppTheme


@Composable
fun TextBase(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = AppTheme.colors.text,
    fontSize: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle
) {

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        style = style
    )

}

@Composable
fun TextBase(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    color: Color = AppTheme.colors.text,
    fontSize: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle
) {

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        style = style
    )

}