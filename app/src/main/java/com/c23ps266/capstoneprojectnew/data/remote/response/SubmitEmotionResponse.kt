package com.c23ps266.capstoneprojectnew.data.remote.response

import com.google.gson.annotations.SerializedName

typealias SubmitEmotionResponse = List<SubmitEmotionResponseItem>

data class SubmitEmotionResponseItem(

	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("title")
	val title: String
)
