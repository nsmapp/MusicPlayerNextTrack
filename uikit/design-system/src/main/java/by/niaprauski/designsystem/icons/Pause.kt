package `by`.niaprauski.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import by.niaprauski.designsystem.theme.Icons

val Icons.Pause: ImageVector
    get() {
        if (_Pause != null) {
            return _Pause!!
        }
        _Pause = ImageVector.Builder(
            name = "Pause",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 6.35f,
            viewportHeight = 6.35f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.529167f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.1087f, 1.5618f)
                curveTo(1.7957f, 2.6456f, 2.5757f, 3.7047f, 2.1261f, 4.7957f)
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.529167f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4.2283f, 1.5637f)
                curveTo(3.9152f, 2.6475f, 4.6952f, 3.7066f, 4.2456f, 4.7976f)
            }
        }.build()

        return _Pause!!
    }

@Suppress("ObjectPropertyName")
private var _Pause: ImageVector? = null
