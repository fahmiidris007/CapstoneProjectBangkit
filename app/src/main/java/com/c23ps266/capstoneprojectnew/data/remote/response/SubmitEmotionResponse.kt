package com.c23ps266.capstoneprojectnew.data.remote.response

import com.google.gson.annotations.SerializedName

/**
 * The response is a json array as its root and contains String for its elements. "Error" response
 * represented as empty list
 */
typealias SubmitEmotionResponse = List<String>
