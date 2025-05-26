package com.example.serfplayer.presentation.viewmodel

import com.example.serfplayer.data.roomdb.MusicEntity
import kotlin.time.Duration

sealed interface PlayerEvent {
    data class Play(val music: MusicEntity): PlayerEvent
    data class PlayPause(val isPlaying: Boolean): PlayerEvent
    object Previos: PlayerEvent
    data class SetShowBottomPlayer(val isShowed:Boolean): PlayerEvent
    object Next:PlayerEvent
    object RefreshMusicList: PlayerEvent
    data class SnapTo(val duration: Long): PlayerEvent
    data class UpdateMusicList(val musicList: List<MusicEntity>) : PlayerEvent
    object ResetIsPaused: PlayerEvent
}