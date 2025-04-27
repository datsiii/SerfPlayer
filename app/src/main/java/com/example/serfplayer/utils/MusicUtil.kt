package com.example.serfplayer.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.serfplayer.data.roomdb.MusicEntity
import androidx.core.net.toUri
import kotlin.time.Duration.Companion.milliseconds

object MusicUtil {
    fun fetchMusicFromDevice(
        context: Context,
        isTracksSmallerThan100KBSkipped: Boolean = true,
        isTracksShorterThan60SecondsSkipped: Boolean = true
    ): List<MusicEntity>{
        val musicList = mutableListOf<MusicEntity>()

        val audioUriExternal = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val musicProjection = listOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.SIZE
        )

        val cursorIndexSongId: Int
        val cursorIndexSongTitle: Int
        val cursorIndexSongArtist: Int
        val cursorIndexSongDuration: Int
        val cursorIndexSongAlbumId: Int
        val cursorIndexSongSize: Int

        val songCursor = context.contentResolver.query(
            audioUriExternal,
            musicProjection.toTypedArray(),
            null,
            null,
            null
        )

        if (songCursor != null){
            cursorIndexSongId = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            cursorIndexSongTitle = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            cursorIndexSongArtist = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            cursorIndexSongDuration = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            cursorIndexSongAlbumId = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            cursorIndexSongSize = songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (songCursor.moveToNext()){
                val audioID = songCursor.getLong(cursorIndexSongId)
                val title = songCursor.getString(cursorIndexSongTitle)
                val artist = songCursor.getString(cursorIndexSongArtist)
                val duration = songCursor.getLong(cursorIndexSongDuration)
                val albumID = songCursor.getString(cursorIndexSongAlbumId)
                val size = songCursor.getInt(cursorIndexSongSize)

                val albumPath = Uri.withAppendedPath("content://media/external/audio/albumart".toUri(), albumID)
                val musicPath = Uri.withAppendedPath(audioUriExternal, "" + audioID)

                val durationGreaterThan60Sec = duration.milliseconds.inWholeSeconds > 60
                val sizeGreaterThan100KB = (size / 1024) > 100

                val music = MusicEntity(
                    audioId = audioID,
                    title = title,
                    artist = if(artist.equals("<unknown>", true)) "Unknown" else artist,
                    duration = duration,
                    albumPath = albumPath.toString(),
                    audioPath = musicPath.toString()
                )

                when{
                    isTracksSmallerThan100KBSkipped and isTracksShorterThan60SecondsSkipped -> {
                        if (sizeGreaterThan100KB and durationGreaterThan60Sec) musicList.add(music)
                    }
                    !isTracksSmallerThan100KBSkipped and isTracksShorterThan60SecondsSkipped -> {
                        if (durationGreaterThan60Sec) musicList.add(music)
                    }
                    isTracksSmallerThan100KBSkipped and !isTracksShorterThan60SecondsSkipped -> {
                        if (sizeGreaterThan100KB) musicList.add(music)
                    }
                    !isTracksSmallerThan100KBSkipped and !isTracksShorterThan60SecondsSkipped -> {
                        musicList.add(music)
                    }
                }
            }
            songCursor.close()
        }

        return musicList
    }
}