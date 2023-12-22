package com.example.flex.data.exercise

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ExerciseInfoAPI {
    @GET("v1/exercises")
    suspend fun getExerciseInfo(@Header("X-Api-Key") key: String, @Query("name") name: String): Response<List<ExerciseInfo>>
}