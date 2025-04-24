package by.niaprauski.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface Navigator {
    val navController: NavHostController
    val navHost: @Composable () -> Unit

    fun navigate(dest: Dest)
}