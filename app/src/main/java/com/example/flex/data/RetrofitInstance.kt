package com.example.flex.data
import retrofit2.converter.gson.GsonConverterFactory
import com.example.flex.data.exercise.ExerciseInfoAPI
import retrofit2.Retrofit

object RetrofitInstance {
    val api: ExerciseInfoAPI by lazy {
        Retrofit.Builder().baseUrl("https://api.api-ninjas.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseInfoAPI::class.java)
    }
}