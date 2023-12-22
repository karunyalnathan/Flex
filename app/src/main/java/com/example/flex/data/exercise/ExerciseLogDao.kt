package com.example.flex.data.exercise

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseLogDao {
    @Query("SELECT * FROM exercise_table ORDER BY name ASC")
    fun getAllExercises(): Flow<List<ExerciseLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: ExerciseLog): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exercise: ExerciseLog)

    @Query("SELECT * FROM exercise_table where name = :exerciseName")
    fun getExercise(exerciseName: String): Flow<ExerciseLog?>

}