package com.example.flex.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey @ColumnInfo(name = "name") val workoutName: String,
    val exercises: List<String>,
    val type: String,
)