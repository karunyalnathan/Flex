package com.example.flex.ui.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flex.FlexApplication
import com.example.flex.WorkoutTypes
import com.example.flex.domain.ExerciseSet
import com.example.flex.data.workoutplan.WorkoutPlan
import com.example.flex.data.workoutplan.WorkoutPlanRepository
import com.example.flex.domain.ActiveWorkoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface WorkoutUIState {
    object Loading : WorkoutUIState
    data class Success(val exercises: List<ExerciseSet>) : WorkoutUIState
    data class Active(val sets: List<ExerciseSet>) : WorkoutUIState
    object ActivityTimer : WorkoutUIState
    object Completed : WorkoutUIState
}

class WorkoutViewModel(
    private val activeWorkoutUseCase: ActiveWorkoutUseCase,
    private val workoutPlanRepository: WorkoutPlanRepository,
    private val flexApplication: FlexApplication
) : ViewModel() {

    private val _uiStateWorkout: MutableStateFlow<WorkoutUIState> =
        MutableStateFlow(WorkoutUIState.Loading)
    val uiState: StateFlow<WorkoutUIState> = _uiStateWorkout.asStateFlow()

    var workoutName: String = ""

    var isActive: Boolean = false
    private val _currentSet: MutableStateFlow<Pair<Int, ExerciseSet>?> = MutableStateFlow(null)
    val currentSet: StateFlow<Pair<Int, ExerciseSet>?> = _currentSet.asStateFlow()

    private val _timer: MutableStateFlow<Pair<Long?, Long?>> = MutableStateFlow(Pair(null, null))
    val timer: StateFlow<Pair<Long?, Long?>> = _timer.asStateFlow()
    private var clock: CountDownTimer? = null
    private var ticker: Job? = null


    fun setWorkout(workoutName: String) {
        this.workoutName = workoutName
        getWorkout()
    }

    private fun getWorkout() {
        if (workoutExpectedToBeEmpty()) {
            _uiStateWorkout.value = WorkoutUIState.Success(listOf())
        } else {
            viewModelScope.launch {
                var isEmpty = true
                while (isEmpty) {
                    activeWorkoutUseCase.invoke(workoutName, isActive).let {
                        if (it.isNotEmpty() && it.size == activeWorkoutUseCase.getWorkoutSize(workoutName)) {
                            _uiStateWorkout.value = WorkoutUIState.Success(it)
                            isEmpty = false
                        }
                    }
                }
            }
        }
    }

    private fun workoutExpectedToBeEmpty(): Boolean {
        return workoutName == WorkoutTypes.Yoga.name
                || workoutName == WorkoutTypes.Run.name
                || workoutName == WorkoutTypes.Pilates.name
    }

    fun startWorkout() {
        viewModelScope.launch {
            isActive = true
            var listEmpty = true
            if (workoutExpectedToBeEmpty()) _uiStateWorkout.value = WorkoutUIState.ActivityTimer
            else {
                while (listEmpty) {
                    activeWorkoutUseCase.invoke(workoutName, true).let {
                        if (it.isNotEmpty()) {
                            _currentSet.value = Pair(0, it.first())
                            _uiStateWorkout.value = WorkoutUIState.Active(it)
                            listEmpty = false
                        }
                    }
                }
            }
        }
    }


    fun updateCurrentExercise(index: Int, newCurrentSet: ExerciseSet) {
        val oldIndex: Int = _currentSet.value?.first ?: 0
        try {
            (uiState.value as WorkoutUIState.Active).sets.let { sets ->
                for (i in oldIndex..index) {
                    sets[i].isCompleted = true
                }
            }
        } catch (e: Exception) {
            Log.e("UpdateCurrentSet", "Issue updating current set")
        }

        if (_currentSet.value?.first != index) {
            cancelTimer()
            _currentSet.value = Pair(index, newCurrentSet)
        }
    }

    fun nextSet(): Boolean {
        try {
            (uiState.value as WorkoutUIState.Active).sets.let { sets ->
                _currentSet.value?.first?.let {
                    if (it + 1 >= sets.size) {
                        completeWorkout()
                        return false
                    } else updateCurrentExercise(it + 1, sets[it + 1])
                }

            }
        } catch (e: Exception) {
            Log.ERROR
        }
        return true
    }

    fun previousSet() {
        try {
            (uiState.value as WorkoutUIState.Active).sets.let { sets ->
                _currentSet.value?.first?.let {
                    if (it >= 0) updateCurrentExercise(it - 1, sets[it - 1])
                }
            }
        } catch (e: Exception) {
            Log.ERROR
        }
    }

    fun endWorkout() {
        isActive = false
        cancelTimer()
        _timer.value = Pair(null, null)
        getWorkout()
    }

    fun completeWorkout() {
        isActive = false
        viewModelScope.launch {
            workoutPlanRepository.get(flexApplication.getPhase()).collect {
                it?.apply {
                    val i: Int = this.workouts.indexOf(workoutName)
                    this.completed[i] = true
                    updatePlan(this)
                }
            }
        }
        _uiStateWorkout.value = WorkoutUIState.Completed
    }

    private fun updatePlan(workoutPlan: WorkoutPlan) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutPlanRepository.update(workoutPlan)
        }
    }

    fun startTimer(time: Long, interval: Long = 1) {
        if (clock == null && ticker == null) {
            clock = getTimer(time, interval)
            ticker = viewModelScope.launch {
                clock?.start()
            }
        }
    }

    private fun cancelTimer() {
        clock?.cancel()
        clock = null
        ticker?.cancel()
        ticker = null
    }

    private fun getTimer(time: Long, secInterval: Long = 1): CountDownTimer {
        return object : CountDownTimer((time * 1000 * secInterval), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                _timer.value = Pair(
                    (millisUntilFinished / (1000 * secInterval)),
                    (millisUntilFinished % (1000 * secInterval)) / 1000
                )
            }

            override fun onFinish() {
                clock = null
                ticker = null
            }
        }
    }
}


class WorkoutViewModelFactory(
    private val activeWorkoutUseCase: ActiveWorkoutUseCase,
    private val workoutPlanRepository: WorkoutPlanRepository,
    private val flexApplication: FlexApplication
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(
                activeWorkoutUseCase,
                workoutPlanRepository,
                flexApplication
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}