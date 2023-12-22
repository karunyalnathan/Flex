package com.example.flex.domain

import com.example.flex.data.exercise.ExerciseRepository
import com.example.flex.data.workout.Workout
import com.example.flex.data.workout.WorkoutRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ActiveWorkoutUseCase(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(workoutName: String, isActive: Boolean): List<ExerciseSet> {
        val workoutList: MutableList<ExerciseSet> = mutableListOf()

        withContext(dispatcher) {

            runBlocking {
                val workout: Workout? = async { getWorkout(workoutName) }.await()

                workout?.exercises?.forEach { exerciseName ->
                    exerciseRepository.getExercise(exerciseName).first()?.let {
                        if (isActive) {
                            for (i in 1..it.sets) {
                                workoutList.add(
                                    ExerciseSet(
                                        name = it.name,
                                        thisSet = i,
                                        totalSets = it.sets,
                                        reps = it.reps,
                                        isTime = it.isTime
                                    )
                                )

                                workoutList.add(
                                    ExerciseSet(
                                        name = "Rest",
                                        thisSet = 0,
                                        totalSets = 0,
                                        reps = it.rest,
                                        isTime = true,
                                        isRest = true
                                    )
                                )
                            }
                        } else {
                            workoutList.add(
                                ExerciseSet(
                                    name = it.name,
                                    thisSet = 0,
                                    totalSets = it.sets,
                                    reps = it.reps,
                                    isTime = it.isTime
                                )
                            )
                        }
                    }
                }
            }
        }
        return workoutList
    }

    suspend fun getWorkoutSize(workoutName: String): Int {
        return workoutRepository.getWorkoutSize(workoutName)
    }

    private suspend fun getWorkout(workoutName: String): Workout? {
        return workoutRepository.getWorkout(workoutName).first()
    }


}
