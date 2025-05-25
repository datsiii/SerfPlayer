package com.example.serfplayer.presentation.music_player_sheet.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.serfplayer.data.roomdb.MusicEntity
import com.example.serfplayer.presentation.viewmodel.PlayerEvent
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel
import com.example.serfplayer.utils.move
import com.example.serfplayer.utils.swap
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun SheetContent(
    isExpanded: Boolean,
    playerViewModel: PlayerViewModel,
    onBack: () -> Unit
){
    val musicUiState by playerViewModel.uiState.collectAsState()
    val musicList = remember { mutableStateListOf<MusicEntity>() }

    val reorderableState = rememberReorderableLazyListState(
        onDragEnd = { from, to ->
            playerViewModel.onEvent(
                PlayerEvent.UpdateMusicList(musicUiState.musicList
                    .toMutableList()
                    .move(from, to))
            )
        },
        onMove = {from, to ->
            musicList.swap(
                musicList.move(from.index, to.index)
            )
        }
    )

    LaunchedEffect(musicUiState.musicList) {
        musicList.swap(musicUiState.musicList)
    }

    BackHandler(isExpanded) {
        onBack()
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .height(16.dp)//xz
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .reorderable(reorderableState)
                .detectReorderAfterLongPress(reorderableState)
        ) {
            items(
                items = musicUiState.musicList,
                key = {item: MusicEntity -> item.hashCode()}
            ){ music ->
                ReorderableItem(
                    reorderableState= reorderableState,
                    key = music.hashCode()
                ) { isDragging ->
                    val elevation by animateDpAsState(
                        targetValue = if(isDragging) 4.dp else 0.dp
                    )
                    val currentAudioId = musicUiState.currentPlayedMusic.audioId
                    SheetMusicItem(
                        music = music,
                        selected = isDragging,
                        elevation = elevation,
                        onClick = onBack
                    )
                }
            }
        }
    }
}