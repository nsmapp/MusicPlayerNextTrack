package by.niaprauski.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.niaprauski.designsystem.theme.AppTheme

@Composable
fun SettingsScreen(){
    Box(
        modifier = Modifier
            .background(color = AppTheme.colors.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
    }
}