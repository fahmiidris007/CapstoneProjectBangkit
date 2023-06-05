package com.c23ps266.capstoneprojectnew.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioModel(
    val title: String,
    val durationMs: Int,
    val uriString: String
) : Parcelable
