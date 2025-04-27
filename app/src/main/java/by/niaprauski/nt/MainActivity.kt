package by.niaprauski.nt

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import by.niaprauski.navigation.NavigatorImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val navigator = remember { NavigatorImpl(this, supportFragmentManager) }
            navigator.navHost()

        }
    }
}

