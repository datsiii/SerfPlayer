package com.example.serfplayer.presentation.music_player_sheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.serfplayer.presentation.viewmodel.PlayerEvent
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by playerViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val isPlaying = uiState.isPlaying
    val currentMusic = uiState.currentPlayedMusic
    val duration = uiState.currentDuration

    // Пример градиентного фона (можно улучшить под обложку)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF121212), Color(0xFF1F1F1F))
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        color = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Обложка альбома
            Image(
                painter = rememberAsyncImagePainter(model = currentMusic.albumPath),
                contentDescription = "Album art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(16.dp, RoundedCornerShape(24.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Название трека и исполнитель
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentMusic.title.ifEmpty { "No track" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentMusic.artist.ifEmpty { "Unknown Artist" },
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.LightGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Прогресс бар с временем
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = duration.toFloat(),
                    onValueChange = { newValue ->
                        scope.launch {
                            playerViewModel.onEvent(PlayerEvent.SnapTo(newValue.toLong()))
                        }
                    },
                    valueRange = 0f..(currentMusic.duration.coerceAtLeast(1).toFloat()),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.Gray
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(duration),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = formatDuration(currentMusic.duration),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопки управления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    scope.launch { playerViewModel.onEvent(PlayerEvent.Previos) }
                }) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(onClick = {
                    scope.launch {
                        playerViewModel.onEvent(PlayerEvent.PlayPause(isPlaying))
                    }
                }) {
                    val icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
                    Icon(
                        imageVector = icon,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(8.dp, CircleShape)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .padding(8.dp)
                    )
                }

                IconButton(onClick = {
                    scope.launch { playerViewModel.onEvent(PlayerEvent.Next) }
                }) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Вспомогательная функция форматирования времени в мм:сс
fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
