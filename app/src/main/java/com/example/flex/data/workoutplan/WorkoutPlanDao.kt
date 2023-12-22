package com.example.flex.data.workoutplan

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanDao {
    @Query("SELECT * FROM workoutPlan_table")
    fun getAllWorkoutPlans(): Flow<List<WorkoutPlan>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: WorkoutPlan): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(workout: WorkoutPlan)

    @Query("SELECT * FROM workoutPlan_table where phase = :workoutPlanName")
    fun getWorkoutPlan(workoutPlanName: String): Flow<WorkoutPlan?>

}