package by.niaprauski.utils.permission

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


object MediaPermissions {
    val permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) READ_MEDIA_AUDIO
        else READ_EXTERNAL_STORAGE

    @Composable
    fun rememberMediaPermissions(): MutableState<Boolean> {

        val context = LocalContext.current

        val hasPermission: MutableState<Boolean> = remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PERMISSION_GRANTED
            )
        }

        return hasPermission
    }

    @Composable
    fun rememberMediaPermissionsLauncher(
        onGranted: () -> Unit,
        onDisablePermissions: () -> Unit,
    ) =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) onGranted() else onDisablePermissions()
        }


}


