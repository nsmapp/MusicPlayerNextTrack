package `by`.niaprauski.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import by.niaprauski.designsystem.theme.Icons

val Icons.PlayPrev: ImageVector
    get() {
        if (_PlayPrev != null) {
            return _PlayPrev!!
        }
        _PlayPrev = ImageVector.Builder(
            name = "PlayPrev",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 6.35f,
            viewportHeight = 6.35f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.430115f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3.9012f, 4.088f)
                curveTo(4.2065f, 4.322f, 4.6033f, 4.5172f, 4.9613f, 4.7288f)
                lineToRelative(-0f, -0f)
                curveToRelative(-0.0808f, -1.0416f, -0.0832f, -2.0795f, -0.0081f, -3.1137f)
                curveTo(4.5863f, 1.829f, 4.063f, 2.1071f, 3.9015f, 2.2367f)
            }
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.41703f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveToRelative(3.8763f, 4.7671f)
                curveToRelative(-0.0948f, -1.0501f, -0.1052f, -2.1092f, -0.0074f, -3.1798f)
                curveTo(2.7568f, 2.1626f, 2.0022f, 2.6785f, 1.475f, 3.1568f)
                curveTo(2.1245f, 3.6917f, 2.9074f, 4.2282f, 3.8763f, 4.7671f)
                close()
            }
        }.build()

        return _PlayPrev!!
    }

@Suppress("ObjectPropertyName")
private var _PlayPrev: ImageVector? = null
