package com.example.flex.data.workout

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    /*gets workout data from room and organizes it to be run */
    @WorkerThread
    suspend fun insert(workout: Workout) {
        workoutDao.insert(workout)
    }

    @WorkerThread
     fun getWorkout(name: String): Flow<Workout?> {
        return workoutDao.getWorkout(name)
    }

    @WorkerThread
    suspend fun getWorkoutSize(name: String): Int {
        return workoutDao.getWorkout(name).first()?.exercises?.size ?: 0
    }

}