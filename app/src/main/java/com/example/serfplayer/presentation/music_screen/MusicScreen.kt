package com.example.serfplayer.presentation.music_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MusicScreen(name: String, modifier: Modifier = Modifier){
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}