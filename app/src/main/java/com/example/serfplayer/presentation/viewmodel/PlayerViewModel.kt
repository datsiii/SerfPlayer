package com.example.serfplayer.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val enviroment: PlayerEnviroment
): StatefulViewModel<MusicUiState>(MusicUiState()) {
    init{
        viewModelScope.launch {
            enviroment.getAllMusics().collect{ musics ->
                updateState { copy(musicList = musics) }

            }
        }
        viewModelScope.launch {
            enviroment.getCurrentPlayedMusic().collect{music->
                updateState { copy(currentPlayedMusic = music) }
            }
        }
        viewModelScope.launch {
            enviroment.isPlaying().collect{isPlaying->
                updateState { copy(isPlaying = isPlaying) }
            }
        }
        viewModelScope.launch {
            enviroment.isBottomMusicPlayerShowed().collect{ isShowed->
                updateState { copy(idBottomPlayerShow = isShowed) }

            }
        }
        viewModelScope.launch {
            enviroment.getCurrentDuration().collect { duration ->
                updateState { copy(currentDuration = duration) }
            }
        }

        viewModelScope.launch {
            enviroment.isPaused().collect { isPaused ->
                updateState { copy(isPaused = isPaused) }
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when(event) {
            is PlayerEvent.Play -> {
                viewModelScope.launch {
                    enviroment.play(event.music)
                }
            }
            is PlayerEvent.PlayPause -> {
                viewModelScope.launch {
                    if(event.isPlaying) enviroment.pause()
                    else enviroment.resume()
                }

            }
            is PlayerEvent.SetShowBottomPlayer -> {
                viewModelScope.launch {
                    enviroment.setShowBottomMusicPlayer(event.isShowed)
                }
            }

            PlayerEvent.RefreshMusicList -> {
                viewModelScope.launch {
                    enviroment.refreshMusicList()
                }
            }

            PlayerEvent.Next -> {
                viewModelScope.launch {
                    enviroment.next()
                }
            }

            is PlayerEvent.SnapTo -> {
                viewModelScope.launch {
                    enviroment.snapTo(event.duration)
                }
            }

            PlayerEvent.Previos -> {
                viewModelScope.launch {
                    enviroment.previous()
                }
            }

            is PlayerEvent.UpdateMusicList -> {
                viewModelScope.launch {
                    enviroment.updateMusicList(event.musicList)
                }
            }

            PlayerEvent.ResetIsPaused -> {
                viewModelScope.launch {
                    enviroment.resetIsPaused()
                }
            }

        }
    }
}