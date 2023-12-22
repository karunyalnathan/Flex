package com.example.flex.data.exercise

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
data class  ExerciseLog(@PrimaryKey @ColumnInfo(name = "name") val name: String,
                        val rest: Int,
                        val sets: Int,
                        val reps: Int,
                        var weight:Int,
                        val isTime: Boolean = false)
// note: when isTime boolean is true, reps is interpreted as a time interval in seconds