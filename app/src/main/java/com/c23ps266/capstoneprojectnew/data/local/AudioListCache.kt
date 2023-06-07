package com.c23ps266.capstoneprojectnew.data.local

import com.c23ps266.capstoneprojectnew.model.AudioModel

object AudioListCache {
    private var audioList: ArrayList<AudioModel> = ArrayList()

    fun getAudioList(): ArrayList<AudioModel> {
        return audioList
    }

    fun setAudioList(list: ArrayList<AudioModel>) {
        audioList.clear()
        audioList.addAll(list)
    }
}