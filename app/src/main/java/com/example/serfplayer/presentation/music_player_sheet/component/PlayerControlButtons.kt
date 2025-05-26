package com.example.serfplayer.presentation.music_player_sheet.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.serfplayer.R

@Composable
fun PlayerControlButtons(
    isPlaying: Boolean,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth(0.8f)
    ) {
        IconButton(
            onClick = onPrevious
        ) {
            Icon(
                painter= painterResource(id = R.drawable.ic_previous),
                contentDescription = null,
                //tint=,
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = MaterialTheme.shapes.large,
            onClick = onPlayPause,
            modifier = Modifier.size(64.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ){
                Icon(
                    painter = painterResource(
                        id = if(!isPlaying) R.drawable.ic_play else R.drawable.ic_pause
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        IconButton(
            onClick = onNext
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                //tint =
            )
        }
    }
}