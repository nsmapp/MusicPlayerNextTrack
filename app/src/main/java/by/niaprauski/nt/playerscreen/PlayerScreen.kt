package by.niaprauski.nt.playerscreen

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.core.content.ContextCompat
import by.niaprauski.nt.models.TrackModel
import kotlinx.coroutines.CoroutineScope


@Composable
fun PlayerScreen() {

    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    val hasStoragePermissions by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_AUDIO
            ) == PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                syncFiles(context.contentResolver)
                text = "Has permission"
            } else {
                text = "Not permission"
                //do nothing
            }
        }

    LaunchedEffect(Unit) {
        if (!hasStoragePermissions) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        }
        else{

            syncFiles(context.contentResolver)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text)
    }

}

fun syncFiles(cr: ContentResolver) {

    val projection: Array<String> = arrayOf(
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
    )

    println("!!!! start sync files")
    val cursor = cr.query(
        /* uri = */ MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        /* projection = */ projection,
        /* selection = */ null,
        /* selectionArgs = */ null,
        /* sortOrder = */ null
    )
    val files = mutableListOf<TrackModel>()
    cursor?.use { c ->
        while (c.moveToNext()){
            val title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

//        val track = TrackModel(
//            title = title,
//            artist = artist,
//            path = path,
//        )
//
//            files.add(track)
        }

    }

    println("!!!! ${files}")
    cursor?.close()



}
