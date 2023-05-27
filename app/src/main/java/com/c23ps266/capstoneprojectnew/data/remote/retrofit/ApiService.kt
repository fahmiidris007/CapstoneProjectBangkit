package com.c23ps266.capstoneprojectnew.data.remote.retrofit

import com.c23ps266.capstoneprojectnew.data.remote.response.SubmitEmotionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    /**
     * Placeholder. TODO: get the API detail from CC then replace this function with the real one
     */
    @GET("/<emotion>")
    suspend fun submitEmotion(
        @Path("emotion")
        emotion: String,
    ): SubmitEmotionResponse
}
