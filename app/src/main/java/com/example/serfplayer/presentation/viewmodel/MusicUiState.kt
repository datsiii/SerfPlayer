package com.example.serfplayer.presentation.viewmodel

import com.example.serfplayer.data.roomdb.MusicEntity

data class MusicUiState(
    val musicList: List<MusicEntity> = emptyList(),
    val currentPlayedMusic: MusicEntity = MusicEntity.default,
    val currentDuration: Long = 0L,
    val isPlaying: Boolean = false,
    val idBottomPlayerShow: Boolean = false
)
