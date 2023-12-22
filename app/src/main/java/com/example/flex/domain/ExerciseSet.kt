package com.example.flex.domain

data class ExerciseSet(val name: String, val thisSet: Int, val totalSets: Int, val reps:Int,
                       val isTime: Boolean = false, val isRest: Boolean = false, var isCompleted: Boolean = false)