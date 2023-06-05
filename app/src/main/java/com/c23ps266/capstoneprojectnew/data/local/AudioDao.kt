package com.c23ps266.capstoneprojectnew.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c23ps266.capstoneprojectnew.data.local.AudioDatabase.Companion.AUDIO_TABLE_NAME
import com.c23ps266.capstoneprojectnew.model.AudioModel

@Dao
interface AudioDao {
    @Query("SELECT * FROM $AUDIO_TABLE_NAME ORDER BY title ASC")
    fun getAll(): LiveData<List<AudioModel>>

    @Query("SELECT * FROM $AUDIO_TABLE_NAME WHERE title = :title")
    fun get(title: String): LiveData<List<AudioModel>>

    @Query("SELECT COUNT(1) FROM $AUDIO_TABLE_NAME WHERE title = :title LIMIT 1")
    fun isExist(title: String): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audio: AudioModel)

    @Query("DELETE FROM $AUDIO_TABLE_NAME WHERE title = :title")
    suspend fun delete(title: String)

    @Query("DELETE FROM $AUDIO_TABLE_NAME")
    suspend fun deleteAll()
}
