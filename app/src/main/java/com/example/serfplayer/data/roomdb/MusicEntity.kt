package com.example.serfplayer.data.roomdb

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Сущность музыки для хранения в базе данных Room.
 *
 * @property audioId Уникальный идентификатор аудиофайла.
 * @property title Название трека.
 * @property artist Исполнитель трека.
 * @property duration Длительность трека в миллисекундах.
 * @property albumPath Путь к обложке альбома.
 * @property audioPath Путь к самому аудиофайлу.
 */
@Parcelize
@Entity
data class MusicEntity(
    @PrimaryKey
    val audioId:Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val albumPath: String,
    val audioPath: String
) : Parcelable
