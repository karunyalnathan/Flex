package com.example.flex.data.exercise

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.flex.data.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.io.IOException

class ExerciseRepository(private val exerciseLogDao: ExerciseLogDao) {

    @WorkerThread
    suspend fun insert(exerciseLog: ExerciseLog) {
        exerciseLogDao.insert(exerciseLog)
    }

    @WorkerThread
    suspend fun update(exerciseLog: ExerciseLog) {
        exerciseLogDao.update(exerciseLog)
    }

    @WorkerThread
    fun getExercise(name: String): Flow<ExerciseLog?> {
        return exerciseLogDao.getExercise(name)
    }





    suspend fun getExerciseInfo(name: String): ExerciseInfo? {
        if (name.isEmpty()) return null
        val response = try {
            RetrofitInstance.api.getExerciseInfo("fiNy+oaMPQvwf5T9/iu6Zw==InUwLShq0RSCy0RS", name)
        } catch (e: IOException) {
            Log.e("ExerciseAPI", "Issue with Network Connection")
        }

        try {
            if ((response as Response<List<ExerciseInfo>>).isSuccessful && response.body() != null) {
                response.body()?.let { if (it.isNotEmpty()) return it[0] }
            }
        } catch (e:Exception) {
            Log.e("ExerciseInfo", "Error Mapping ExerciseInfo")
        }
        return null
    }

}