package com.example.serfplayer.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Query("SELECT * FROM MusicEntity")
    fun getAllMusic(): Flow<List<MusicEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg music: MusicEntity)

    @Delete
    suspend fun delete(vararg music: MusicEntity)

}