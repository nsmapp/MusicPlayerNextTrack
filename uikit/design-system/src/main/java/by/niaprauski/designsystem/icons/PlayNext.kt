package `by`.niaprauski.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import by.niaprauski.designsystem.theme.Icons

val Icons.PlayNext: ImageVector
    get() {
        if (_PlayNext != null) {
            return _PlayNext!!
        }
        _PlayNext = ImageVector.Builder(
            name = "PlayNext",
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
                moveTo(2.5417f, 2.2663f)
                curveTo(2.2364f, 2.0323f, 1.8396f, 1.8371f, 1.4815f, 1.6255f)
                verticalLineToRelative(0f)
                curveToRelative(0.0808f, 1.0416f, 0.0832f, 2.0795f, 0.0081f, 3.1137f)
                curveTo(1.8566f, 4.5253f, 2.3799f, 4.2472f, 2.5414f, 4.1176f)
            }
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.41703f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveToRelative(2.5666f, 1.5872f)
                curveToRelative(0.0948f, 1.0501f, 0.1052f, 2.1092f, 0.0074f, 3.1798f)
                curveTo(3.6861f, 4.1917f, 4.4407f, 3.6758f, 4.9679f, 3.1975f)
                curveTo(4.3184f, 2.6626f, 3.5355f, 2.1261f, 2.5666f, 1.5872f)
                close()
            }
        }.build()

        return _PlayNext!!
    }

@Suppress("ObjectPropertyName")
private var _PlayNext: ImageVector? = null
