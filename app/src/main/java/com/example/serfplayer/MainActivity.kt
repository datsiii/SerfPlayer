package com.example.serfplayer

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.serfplayer.presentation.music_screen.MusicScreen
import com.example.serfplayer.presentation.permission.CheckAndRequestPermissions
import com.example.serfplayer.ui.theme.SerfPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listOfPermission = mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                add(Manifest.permission.READ_MEDIA_AUDIO)
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
            else{
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        setContent {
            SerfPlayerTheme {
                CheckAndRequestPermissions(
                    permissions= listOfPermission
                ){
                    MusicScreen(name = "Android", modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

