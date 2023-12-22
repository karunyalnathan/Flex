package com.example.flex.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.flex.FlexApplication
import com.example.flex.data.workoutplan.WorkoutPlan
import com.example.flex.data.workoutplan.WorkoutPlanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeUIState {
    object Loading : HomeUIState
    data class Success(val workoutPlan: WorkoutPlan) : HomeUIState
}

class HomeViewModel(
    private val repository: WorkoutPlanRepository,
    application: Application
) : AndroidViewModel(application) {


    private val workoutPlan: Flow<List<WorkoutPlan>> = repository.workoutPlans
    private lateinit var workoutMap: Map<String, WorkoutPlan>

    private val _phase: MutableStateFlow<String> =
        MutableStateFlow((application as FlexApplication).getPhase())
    val phase: StateFlow<String> = _phase.asStateFlow()

    private val _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState.Loading)
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            workoutPlan.collect { workoutPlans ->
                workoutMap = workoutPlans.associateBy { it.phase }
                if (phase.value.isNotEmpty()) getPlan()
            }
        }
    }

    private fun getPlan() {
        workoutMap[phase.value]?.let { _uiState.value = HomeUIState.Success(it) }
    }

    fun getPhases(): List<String> = workoutMap.keys.toList()
    fun updatePhase(phase: String) {
        _phase.value = phase
        workoutMap[phase]?.let { _uiState.value = HomeUIState.Success(it) }
    }

    fun resetPhase(phase: String) {
        viewModelScope.launch(Dispatchers.Default) {
            workoutMap[phase]?.let {
                it.apply {
                    for (i in 0 until it.completed.size) it.completed[i] = false
                    repository.update(it)
                }
            }
        }
    }
}

class HomeViewModelFactory(
    private val repository: WorkoutPlanRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}