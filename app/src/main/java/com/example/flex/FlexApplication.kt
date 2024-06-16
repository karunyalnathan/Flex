package com.example.flex

import android.app.Application
import android.content.Context
import com.example.flex.data.exercise.ExerciseLogDatabase
import com.example.flex.data.exercise.ExerciseRepository
import com.example.flex.data.workout.WorkoutDatabase
import com.example.flex.data.workout.WorkoutRepository
import com.example.flex.data.workoutplan.WorkoutPlanDatabase
import com.example.flex.data.workoutplan.WorkoutPlanRepository
import com.example.flex.domain.ActiveWorkoutUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Suppress("PrivatePropertyName")
class FlexApplication: Application() {
    private val SHARED_PREFERENCES_NAME = "com.example.flex.sharedpreferences"
    private val CURRENT_PHASE = "CURRENT_PHASE"

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val exerciseLogDatabase by lazy { ExerciseLogDatabase.getDatabase(this, applicationScope) }
    val exerciseRepository by lazy { ExerciseRepository(exerciseLogDatabase.exerciseDao()) }
    private val workoutPlanDatabase by lazy { WorkoutPlanDatabase.getDatabase(this, applicationScope) }
    val workoutPlanRepository by lazy { WorkoutPlanRepository(workoutPlanDatabase.workoutPlanDao()) }
    private val workoutDatabase by lazy { WorkoutDatabase.getDatabase(this, applicationScope) }
    private val workoutRepository by lazy { WorkoutRepository(workoutDatabase.workoutDao()) }

    val activeWorkoutUseCase by lazy { ActiveWorkoutUseCase(workoutRepository, exerciseRepository) }

    fun setPhase(phase: String) {
        val sharedPref = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        applicationScope.launch {
            with(sharedPref.edit()) {
                    putString(CURRENT_PHASE, phase)
                    apply()
            }
        }
    }

    fun getPhase(): String {
        val sharedPref = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(CURRENT_PHASE, "") ?: ""
    }
}