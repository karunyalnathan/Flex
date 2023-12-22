package com.example.flex.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flex.data.exercise.ExerciseInfo
import com.example.flex.data.exercise.ExerciseLog
import com.example.flex.data.exercise.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ExerciseLogUIState {
    object Loading : ExerciseLogUIState
    data class Success(val exercise: ExerciseLog?) : ExerciseLogUIState
    object Rest : ExerciseLogUIState
}

sealed interface ExerciseInfoUIState {
    object Loading : ExerciseInfoUIState
    data class Success(val exerciseInfo: ExerciseInfo?) : ExerciseInfoUIState
}

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {

    private val _exerciseName = MutableStateFlow("")
    private val exerciseName: StateFlow<String> = _exerciseName.asStateFlow()

    private val _exerciseLog: MutableStateFlow<ExerciseLogUIState> = MutableStateFlow(
        ExerciseLogUIState.Loading
    )
    val exerciseLog: StateFlow<ExerciseLogUIState> = _exerciseLog.asStateFlow()

    private val _exerciseInfo: MutableStateFlow<ExerciseInfoUIState> = MutableStateFlow(
        ExerciseInfoUIState.Loading
    )
    val exerciseInfo: StateFlow<ExerciseInfoUIState> = _exerciseInfo.asStateFlow()


    init {
        update(exerciseName.value)
    }

    fun update(name: String) {
        _exerciseLog.value = ExerciseLogUIState.Loading

        _exerciseName.update { name }
        if (name == "Rest") {
            _exerciseLog.value = ExerciseLogUIState.Rest
            _exerciseInfo.value = ExerciseInfoUIState.Loading
            return
        }
        viewModelScope.launch {
            repository.getExercise(name = exerciseName.value).collect {
                it?.let { _exerciseLog.value = ExerciseLogUIState.Success(it) }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _exerciseInfo.value =
                ExerciseInfoUIState.Success(repository.getExerciseInfo(name = exerciseName.value))
        }
    }


    fun updateWeightUsed(weight: Int, exercise: ExerciseLog) {
        viewModelScope.launch {
            repository.update(exercise.apply { this.weight = weight })
        }
    }
}

class ExerciseViewModelFactory(private val repository: ExerciseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}