package com.example.serfplayer.utils

import android.content.Context
import com.example.serfplayer.data.roomdb.MusicEntity

object MusicUtil {
    fun fetchMusicFromDevice(
        context: Context,
        isTracksSmallerThan100KBSkipped: Boolean = true,
        isTracksShorterThan60SecondsSkipped: Boolean = true
    ): List<MusicEntity>{
        val musicList = mutableListOf<MusicEntity>()

        return musicList
    }
}