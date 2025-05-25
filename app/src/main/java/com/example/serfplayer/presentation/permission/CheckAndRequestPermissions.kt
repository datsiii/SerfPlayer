package com.example.serfplayer.presentation.permission

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.example.serfplayer.ui.theme.Dimens
import com.example.serfplayer.R
import android.net.Uri

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckAndRequestPermissions(
    permissions: List<String>,
    appContent: @Composable () -> Unit
){
    val context = LocalContext.current
    val activity = LocalActivity.current as Activity

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    var isPermissionDenied by remember { mutableStateOf(false) }

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(Dimens.Six)
            ){
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.music_player_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .background(color = Color.White)
                )
                Spacer(modifier = Modifier.height(Dimens.Six))
                Text(
                    text = "Please access permissions",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(Dimens.Six))
                TextButton(
                    onClick = {permissionState.launchMultiplePermissionRequest()},
                    modifier = Modifier.fillMaxWidth(),
                    colors =  ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(Dimens.Three)
                ) {
                    Text(text = "Enable permission")
                }
            }
        },
        permissionsNotAvailableContent = {
            isPermissionDenied = true
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(Dimens.Six)
            ){
                Text(
                    text = "permission rationale",
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(Dimens.Six))
                TextButton(
                    onClick = { activity.openAppSettings() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(Dimens.Three)
                ) {
                    Text(text = "Go to settings")
                }
            }
        }
    ){
        appContent.invoke()
    }
}

private fun Activity.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(this)
    }
}