package com.example.serfplayer.presentation.music_player_sheet.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel
import com.example.serfplayer.R
import com.example.serfplayer.presentation.viewmodel.PlayerEvent

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionContent(
    playerViewModel: PlayerViewModel,
    fraction: Float,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val musicUiState by playerViewModel.uiState.collectAsState()
    val motionScene = remember{
        context.resources
            .openRawResource(R.raw.motion_scene)
            .readBytes()
            .decodeToString()
    }
    Row (modifier = modifier){
        MotionLayout(
            motionScene = MotionScene(content = motionScene),
            progress = fraction,
            modifier = Modifier.fillMaxSize()
        ){
            Spacer(modifier = Modifier.layoutId("top_bar"))
            AlbumImage(
                albumPath = musicUiState.currentPlayedMusic.albumPath,
                modifier = Modifier
                    .layoutId("album_image")
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f, true)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.layoutId("column_title_artist")
            ) {
                AnimatedVisibility(visible = fraction < 0.8f) {
                    Spacer(Modifier.height(16.dp))
                }
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = musicUiState.currentPlayedMusic.artist,
                    textAlign = if (fraction > 0.8f) TextAlign.Start else TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.Black,
                        fontSize = if (fraction > 0.8f) MaterialTheme.typography.titleSmall.fontSize
                            else MaterialTheme.typography.titleMedium.fontSize
                    ),
                    modifier = Modifier.fillMaxWidth(if (fraction>0.8f) 1f else 0.7f)
                )
            }

            Row (modifier = Modifier.layoutId("top_player_buttons")){
                IconButton(onClick = {
                    playerViewModel.onEvent(PlayerEvent.PlayPause(musicUiState.isPlaying))
                }) {
                    Icon(
                        painter = painterResource(
                            id = if (!musicUiState.isPlaying) R.drawable.ic_play else R.drawable.ic_pause
                        ),
                        contentDescription = null,
                        tint = Color(0xFFEEEEEE)
                    )
                }

                IconButton(onClick = {
                    playerViewModel.onEvent(PlayerEvent.Next)
                }) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_next
                        ),
                        contentDescription = null,
                        tint = Color(0xFFEEEEEE)
                    )
                }
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.layoutId("main_play_control")
            ){
                Spacer(modifier = Modifier.height(24.dp))
                PlayingProgress(
                    maxDuration = musicUiState.currentPlayedMusic.duration,
                    currentDuration = musicUiState.currentDuration,
                    onChange = { progress->
                        val duration = progress*musicUiState.currentPlayedMusic.duration

                        playerViewModel.onEvent(PlayerEvent.SnapTo(duration.toLong()))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PlayerControlButtons(
                    isPlaying = musicUiState.isPlaying,
                    onPrevious = {playerViewModel.onEvent(PlayerEvent.Previos)},
                    onPlayPause = {
                        playerViewModel.onEvent(PlayerEvent.PlayPause(musicUiState.isPlaying))
                    },
                    onNext = {playerViewModel.onEvent(PlayerEvent.Next)}
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}