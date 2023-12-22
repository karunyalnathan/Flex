package com.example.flex.data.workoutplan

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workoutPlan_table")
data class WorkoutPlan(
    @PrimaryKey @ColumnInfo(name = "phase") val phase: String,
    val workouts: List<String>,
    val completed: MutableList<Boolean>
)