package com.c23ps266.capstoneprojectnew.data.remote.retrofit

import com.c23ps266.capstoneprojectnew.data.remote.response.SubmitEmotionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{emotion}")
    suspend fun submitEmotion(
        @Path("emotion")
        emotion: String,
    ): SubmitEmotionResponse
}
