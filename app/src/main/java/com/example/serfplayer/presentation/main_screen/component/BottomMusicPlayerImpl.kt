package com.example.serfplayer.presentation.main_screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.serfplayer.navigation.Routes
import com.example.serfplayer.presentation.viewmodel.MusicUiState

@Composable
fun BoxScope.BottomMusicPlayerImpl(
    navController: NavController,
    musicUiState: MusicUiState,
    onPlayPauseClicked: (isPlaying: Boolean) -> Unit
){
    AnimatedVisibility(
        visible = musicUiState.idBottomPlayerShow,
        enter = slideInVertically( initialOffsetY = {it}),
        exit = slideOutVertically(targetOffsetY = {it}),
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
    ) {
        BottomMusicPlayer(
            currentMusic = musicUiState.currentPlayedMusic,
            currentDuration = musicUiState.currentDuration,
            isPlaying = musicUiState.isPlaying,
            onClick = {
                navController.navigate(Routes.Player.name)
            },
            onPlayPauseClicked= onPlayPauseClicked
        )
    }
}