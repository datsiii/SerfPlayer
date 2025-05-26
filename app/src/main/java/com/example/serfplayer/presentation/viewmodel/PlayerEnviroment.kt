package com.example.serfplayer.presentation.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.serfplayer.data.roomdb.MusicEntity
import com.example.serfplayer.data.roomdb.MusicRepository
import com.example.serfplayer.utils.MusicUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerEnviroment @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicRepository: MusicRepository
) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _allMusics = MutableStateFlow(emptyList<MusicEntity>())
    private val allMusics: StateFlow<List<MusicEntity>> = _allMusics.asStateFlow()

    private val _currentPlayedMusic = MutableStateFlow(MusicEntity.default)
    private val currentPlayedMusic: StateFlow<MusicEntity> = _currentPlayedMusic

    private val _currentDuration = MutableStateFlow(0L)
    private val currentDuration: StateFlow<Long> = _currentDuration

    private val _isPlaying = MutableStateFlow(false)
    private val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _playbackMode = MutableStateFlow(PlaybackMode.REPEAT_ALL)
    private val playbackMode: StateFlow<PlaybackMode> = _playbackMode

    private val _hasStopped = MutableStateFlow(false)
    private val hasStopped: StateFlow<Boolean> = _hasStopped

    private val _isBottomMusicPlayerShowed = MutableStateFlow(false)
    private val isBottomMusicPlayerShowed: StateFlow<Boolean> = _isBottomMusicPlayerShowed

    private val _isPaused = MutableStateFlow(false)
    private val isPaused: StateFlow<Boolean> = _isPaused

    private val playerHandler: Handler = Handler(Looper.getMainLooper())

    private var playingRunnable: Runnable = Runnable { }
    private var playingHandler: Handler = Handler(Looper.getMainLooper())

        private val exoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            @Override
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    when(playbackMode.value){
                        PlaybackMode.REPEAT_ONE -> {
                            CoroutineScope(dispatcher).launch { play(currentPlayedMusic.value) }
                        }
                        PlaybackMode.REPEAT_ALL -> {
                            val currentIndex = allMusics.value.indexOfFirst {
                                it.audioId == currentPlayedMusic.value.audioId
                            }

                            val nextSong = when{
                                currentIndex == allMusics.value.lastIndex -> allMusics.value[0]
                                currentIndex != -1 -> allMusics.value[currentIndex + 1]
                                else -> allMusics.value[0]
                            }

                            CoroutineScope(dispatcher).launch {
                                play(nextSong)
                            }
                        }
                        PlaybackMode.REPEAT_OFF -> {
                            this@apply.stop()
                            _currentPlayedMusic.tryEmit(MusicEntity.default)
                        }
                    }
                }
            }

            @Override
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _isPlaying.tryEmit(isPlaying)
            }
        })
    }

    init {
        CoroutineScope(dispatcher).launch {
            musicRepository.getAllMusic().distinctUntilChanged().collect{
                _allMusics.emit(it)
            }
        }
    }

    fun getAllMusics(): Flow<List<MusicEntity>> {
        return allMusics
    }

    fun getCurrentPlayedMusic(): Flow<MusicEntity> {
        return currentPlayedMusic
    }

    fun isPlaying() : Flow<Boolean>{
        return isPlaying
    }

    fun isBottomMusicPlayerShowed(): Flow<Boolean> {
        return isBottomMusicPlayerShowed
    }

    fun getCurrentDuration() : Flow<Long> = currentDuration

    fun isPaused(): Flow<Boolean> = isPaused

    suspend fun resetIsPaused(){
        _isPaused.emit(false)
    }

    suspend fun play(music: MusicEntity){
        if(music.audioId != MusicEntity.default.audioId){
            _hasStopped.emit(false)
            _currentPlayedMusic.emit(music)

            playerHandler.post {
                exoPlayer.setMediaItem(MediaItem.fromUri(music.audioPath.toUri()))
                exoPlayer.prepare()
                exoPlayer.play()
            }

            playingRunnable = Runnable {
                val duration = if (exoPlayer.duration != -1L) exoPlayer.currentPosition else 0L
                _currentDuration.tryEmit(duration)
            }
            playingHandler.post(playingRunnable)
        }
    }

    suspend fun pause(){
        playerHandler.post{exoPlayer.pause()}
        _isPaused.emit(true)
    }

    suspend fun resume(){
        if(hasStopped.value && currentPlayedMusic.value != MusicEntity.default){
            play(currentPlayedMusic.value)
        }
        else playerHandler.post{exoPlayer.play()}
    }

    suspend fun previous(){
        val currentIndex = allMusics.value.indexOfFirst {
            it.audioId == currentPlayedMusic.value.audioId
        }
        val previousMusic = when{
            currentIndex == 0 -> allMusics.value[allMusics.value.lastIndex]
            currentIndex >= 1 -> allMusics.value[currentIndex-1]
            else->allMusics.value[0]
        }

        CoroutineScope(dispatcher).launch { play(previousMusic) }
    }

    suspend fun next(){
        val currentIndex = allMusics.value.indexOfFirst {
            it.audioId == currentPlayedMusic.value.audioId
        }
        val nextMusic = when{
            currentIndex == allMusics.value.lastIndex -> allMusics.value[0]
            currentIndex != -1 -> allMusics.value[currentIndex+1]
            else -> allMusics.value[0]
        }
        CoroutineScope(dispatcher).launch { play(nextMusic) }
    }

    suspend fun snapTo(
        duration: Long,
        fromUser: Boolean = true
    ){
        _currentDuration.tryEmit(duration)
        if(fromUser) playerHandler.post { exoPlayer.seekTo(duration) }
    }

    suspend fun setShowBottomMusicPlayer(isShowed: Boolean) {
        _isBottomMusicPlayerShowed.emit(isShowed)
    }

    suspend fun updateMusicList(musicList: List<MusicEntity>){
        _allMusics.emit(musicList)
    }

    suspend fun refreshMusicList(){
        val scannedMusics = MusicUtil.fetchMusicFromDevice(context = context)
        insertAllMusics(scannedMusics)
    }

    private suspend fun insertAllMusics(newMusicList: List<MusicEntity>){
        val musicsToInsert = arrayListOf<MusicEntity>()
        val musicsToDelete = arrayListOf<MusicEntity>()

        val storedMusicsIDs = allMusics.value.map {it.audioId}
        val newMusicsIDs  = allMusics.value.map { it.audioId }

        newMusicList.forEach {
            if(it.audioId !in storedMusicsIDs) musicsToInsert.add(it)
        }

        allMusics.value.forEach {
            if(it.audioId !in newMusicsIDs) musicsToDelete.add(it)
        }

        musicRepository.insertMusics(*musicsToInsert.toTypedArray())
        musicRepository.deleteMusics(*musicsToDelete.toTypedArray())
    }
}

enum class PlaybackMode{
    REPEAT_ONE,
    REPEAT_ALL,
    REPEAT_OFF,
}