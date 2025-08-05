package `by`.niaprauski.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import by.niaprauski.designsystem.theme.Icons

val Icons.Stop: ImageVector
    get() {
        if (_Stop != null) {
            return _Stop!!
        }
        _Stop = ImageVector.Builder(
            name = "Stop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 6.35f,
            viewportHeight = 6.35f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 0.529167f,
                strokeLineCap = StrokeCap.Square,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(1.335f, 1.3193f)
                curveTo(2.6301f, 1.1414f, 3.7695f, 1.5314f, 5f, 1.3191f)
                curveTo(5.2361f, 2.5474f, 4.7458f, 3.7899f, 5.0287f, 5.03f)
                curveTo(3.8516f, 5.2076f, 2.5437f, 4.766f, 1.3189f, 5.0126f)
                curveTo(1.1227f, 3.7074f, 1.5039f, 2.5856f, 1.335f, 1.3193f)
                close()
            }
        }.build()

        return _Stop!!
    }

@Suppress("ObjectPropertyName")
private var _Stop: ImageVector? = null
