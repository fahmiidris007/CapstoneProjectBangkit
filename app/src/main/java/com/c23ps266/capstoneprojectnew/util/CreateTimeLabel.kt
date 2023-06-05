package com.c23ps266.capstoneprojectnew.util

import java.util.concurrent.TimeUnit

fun createTimeLabel(time: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(time.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(time.toLong()) -
            TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%02d:%02d", minutes, seconds)
}