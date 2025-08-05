package `by`.niaprauski.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import by.niaprauski.designsystem.theme.Icons

val Icons.Play: ImageVector
    get() {
        if (_Play != null) {
            return _Play!!
        }
        _Play = ImageVector.Builder(
            name = "Play",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 6.35f,
            viewportHeight = 6.35f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.518341f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveToRelative(1.6144f, 1.3171f)
                curveToRelative(0.1279f, 1.0315f, 0.134f, 2.2433f, 0.01f, 3.6474f)
                curveTo(3.1747f, 4.3609f, 4.0816f, 3.762f, 4.8485f, 3.1642f)
                curveTo(3.9255f, 2.4476f, 2.8565f, 1.826f, 1.6144f, 1.3171f)
                close()
            }
        }.build()

        return _Play!!
    }

@Suppress("ObjectPropertyName")
private var _Play: ImageVector? = null
