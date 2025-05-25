package com.example.serfplayer.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.serfplayer.data.roomdb.MusicEntity
import com.example.serfplayer.presentation.main_screen.component.BottomMusicPlayerHeight
import com.example.serfplayer.presentation.main_screen.component.MusicItem
import com.example.serfplayer.ui.theme.Dimens
import com.example.serfplayer.presentation.main_screen.component.BottomMusicPlayerImpl
import com.example.serfplayer.presentation.viewmodel.MusicUiState
import com.example.serfplayer.presentation.viewmodel.PlayerEvent
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel

private const val TAG = "MusicScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    playerViewModel: PlayerViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val musicUiState by playerViewModel.uiState.collectAsState()

    LaunchedEffect(musicUiState.currentPlayedMusic) {
        val isShowed = (musicUiState.currentPlayedMusic != MusicEntity.default)
        playerViewModel.onEvent(PlayerEvent.SetShowBottomPlayer(isShowed))
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(Dimens.One))
            CenterAlignedTopAppBar(
                title = { Text("SerfPlayer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            MusicListContent(musicUiState= musicUiState) {music->
                playerViewModel.onEvent(PlayerEvent.Play(music))
            }
        }
        BottomMusicPlayerImpl(
            navController = navController,
            musicUiState = musicUiState
        ){ isPlaying->
            playerViewModel.onEvent(PlayerEvent.PlayPause(isPlaying))
        }
    }

    ComposableLifeCycle { _, event ->
        when(event){

            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "Music screen on resume")
                playerViewModel.onEvent(PlayerEvent.RefreshMusicList)
            }
            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "Music screen on pause")

            }
            else -> {

            }
        }
    }
}

@Composable
fun ComposableLifeCycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event ) -> Unit
){
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun MusicListContent(
    musicUiState: MusicUiState,
    onSelectedMusic: (music: MusicEntity) -> Unit
){
    LazyColumn(
        modifier =  Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        val currentAudioId = musicUiState.currentPlayedMusic.audioId
        itemsIndexed(musicUiState.musicList){_, music ->
            MusicItem(
                music = music,
                selected = (music.audioId == currentAudioId ),
                isMusicPlaying = musicUiState.isPlaying,
                onClick = {onSelectedMusic.invoke(music)}
            )
        }
        //BottomMusicPlayer padding
        item{ Spacer(modifier = Modifier.height(BottomMusicPlayerHeight.value)) }

    }

}