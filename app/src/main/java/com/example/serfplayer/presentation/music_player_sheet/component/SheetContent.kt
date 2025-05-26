package com.example.serfplayer.presentation.music_player_sheet.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.serfplayer.data.roomdb.MusicEntity
import com.example.serfplayer.presentation.viewmodel.PlayerEvent
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel

@Composable
fun SheetContent(
    isExpanded: Boolean,
    playerViewModel: PlayerViewModel,
    onBack: () -> Unit
) {
    val musicUiState by playerViewModel.uiState.collectAsState()

    BackHandler(isExpanded) {
        onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)  // ручка сверху, можно 12 или 16 dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }

        Text(
            text = "Items count: ${musicUiState.musicList.size}",
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = musicUiState.musicList,
                key = { item -> item.audioId } // лучше уникальный и стабильный ключ
            ) { music ->
                val elevation by animateDpAsState(
                    targetValue = if (music.audioId == musicUiState.currentPlayedMusic.audioId) 4.dp else 0.dp
                )
                SheetMusicItem(
                    music = music,
                    selected = music.audioId == musicUiState.currentPlayedMusic.audioId,
                    elevation = elevation,
                    onClick = onBack
                )
            }
        }
    }
}

