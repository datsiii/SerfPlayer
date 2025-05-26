package com.example.serfplayer.presentation.music_player_sheet.component

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.serfplayer.ui.theme.Purple40
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PlayingProgress(
    maxDuration: Long,
    currentDuration: Long,
    onChange: (Float) -> Unit
){
    val progress = remember(maxDuration, currentDuration) {
        if (maxDuration > 0) {
            currentDuration.toFloat() / maxDuration.toFloat()
        } else {
            0f
        }
    }


    val maxDurationInMinute = remember(maxDuration) {
        maxDuration.milliseconds.inWholeMinutes
    }

    val maxDurationInSeconds = remember(maxDuration) {
        maxDuration.milliseconds.inWholeSeconds % 60
    }

    val currentDurationInMinute = remember(currentDuration) {
        maxDuration.milliseconds.inWholeMinutes
    }

    val currentDurationInSeconds = remember(currentDuration) {
        maxDuration.milliseconds.inWholeSeconds % 60
    }

    val maxDurationString = remember(maxDurationInMinute, maxDurationInSeconds) {
        val minute = if(maxDurationInMinute < 10) "0$maxDurationInMinute"
        else maxDurationInMinute.toString()

        val second = if(maxDurationInSeconds < 10) "0$maxDurationInSeconds"
        else maxDurationInSeconds.toString()

        return@remember "$minute:$second"
    }

    val currentDurationString = remember(currentDurationInMinute, currentDurationInSeconds) {
        val minute = if(currentDurationInMinute < 10) "0$currentDurationInMinute"
        else currentDurationInMinute.toString()

        val second = if(currentDurationInSeconds < 10) "0$currentDurationInSeconds"
        else currentDurationInSeconds.toString()

        return@remember "$minute:$second"
    }

    Column(
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Slider(
            value = progress,
            onValueChange = onChange,
            colors = SliderDefaults.colors(
                thumbColor = Purple40,
                activeTrackColor = Purple40
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = currentDurationString,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Black
                )
            )
            Text(
                text = maxDurationString,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Black
                )
            )
        }
    }
}