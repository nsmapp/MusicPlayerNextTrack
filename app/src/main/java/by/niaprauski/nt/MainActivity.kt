package by.niaprauski.nt

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.navigation.NavigatorImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            AppTheme(isDarkThemeEnabled = false) {

                val navigator = remember { NavigatorImpl(this, supportFragmentManager) }
                navigator.navHost()
            }

        }
    }
}

