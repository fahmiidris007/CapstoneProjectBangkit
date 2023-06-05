package com.c23ps266.capstoneprojectnew.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.c23ps266.capstoneprojectnew.data.local.AudioDatabase
import kotlinx.parcelize.Parcelize

@Entity(tableName = AudioDatabase.AUDIO_TABLE_NAME)
@Parcelize
data class AudioModel(
    @PrimaryKey
    val title: String,

    @ColumnInfo(name = "duration_ms")
    val durationMs: Int,

    @ColumnInfo(name = "uri_string")
    val uriString: String
) : Parcelable
