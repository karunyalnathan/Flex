package com.example.flex.data.workoutplan

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class WorkoutPlanRepository(private val workoutPlanDao: WorkoutPlanDao) {
    /*gets workout data from room and organizes it to be run */
    val workoutPlans = (workoutPlanDao.getAllWorkoutPlans())

    @WorkerThread
    suspend fun insert(workoutPlan: WorkoutPlan) {
        workoutPlanDao.insert(workoutPlan)
    }

    @WorkerThread
    fun update(workoutPlan: WorkoutPlan) {
        workoutPlanDao.update(workoutPlan)
    }

    @WorkerThread
    fun get(workoutPlanName: String): Flow<WorkoutPlan?> {
        return workoutPlanDao.getWorkoutPlan(workoutPlanName)
    }
}