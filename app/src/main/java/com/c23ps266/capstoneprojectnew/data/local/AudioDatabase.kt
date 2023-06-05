package com.c23ps266.capstoneprojectnew.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.c23ps266.capstoneprojectnew.model.AudioModel

@Database(entities = [AudioModel::class], version = 1, exportSchema = false)
abstract class AudioDatabase : RoomDatabase() {
    abstract fun audioDao(): AudioDao

    companion object {
        private const val DATABASE_NAME = "data.db"
        const val AUDIO_TABLE_NAME = "audio"

        @Volatile
        private var INSTANCE: AudioDatabase? = null

        fun getInstance(context: Context): AudioDatabase = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, AudioDatabase::class.java, DATABASE_NAME).build().also {
                INSTANCE = it
            }
        }
    }
}
