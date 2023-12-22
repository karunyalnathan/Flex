package com.example.flex.data.workout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flex.WorkoutTypes
import com.example.flex.data.TypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Workout::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)

 abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao


    private class WorkoutDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    initWorkoutData(database.workoutDao())
                }
            }
        }

        suspend fun initWorkoutData(workoutDao: WorkoutDao) {
            workoutDao.insert(
                Workout(
                    "Low Intensity Lift 1",
                    listOf("Barbell back squat to box", "Barbell Hip Thrust", "Pushups", "Biceps curl to shoulder press"),
                    WorkoutTypes.Strength.name
                )
            )
            workoutDao.insert(
                Workout(
                    "Low Intensity Lift 2",
                    listOf("Barbell Deadlift", "Bent Over Two-Arm Long Bar Row", "Romanian Deadlift With Dumbbells", "Pullups"),
                    WorkoutTypes.Strength.name
                )
            )
            workoutDao.insert(
                Workout(
                    "Low Intensity Lift 3",
                    listOf("Inchworm", "Barbell Hip Thrust", "Single-leg glute bridge", "Romanian Deadlift With Dumbbells"),
                    WorkoutTypes.Strength.name
                )
            )
            workoutDao.insert(
                Workout(
                    "High Intensity Lift 1",
                    listOf("Elbow plank", "Barbell Full Squat", "Barbell Hip Thrust", "Box jump", "Dumbbell Bench Press", "Biceps curl to shoulder press", "Push Up to Side Plank"),
                    WorkoutTypes.Strength.name
                )
            )
            workoutDao.insert(
                Workout(
                    "High Intensity Lift 2",
                    listOf("Elbow plank", "Barbell Deadlift", "Scissors Jump", "Bent Over Two-Arm Long Bar Row", "Romanian Deadlift With Dumbbells", "Pullups"),
                    WorkoutTypes.Strength.name
                )
            )
            workoutDao.insert(
                Workout(
                    "High Intensity Lift 3",
                    listOf("Elbow plank", "Sumo Deadlift", "Single-Leg Press", "Box jump", "Romanian Deadlift With Dumbbells", "Dumbbell farmer's walk"),
                    WorkoutTypes.Strength.name
                )
            )
            workoutDao.insert(
                Workout(
                    "HIIT 1",
                    listOf("Jumping rope", "Elbow plank", "Dumbbell farmer's walk", "Dumbbell lunges", "Broad jump"),
                    WorkoutTypes.HIIT.name
                )
            )
            workoutDao.insert(
                Workout(
                    "HIIT 2",
                    listOf("Jumping rope", "Elbow plank", "Dumbbell farmer's walk", "Dumbbell Flyes", "Landmine twist", "Dumbbell front raise to lateral raise", "Dumbbell lunges"),
                    WorkoutTypes.HIIT.name
                )
            )
        }
    }
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): WorkoutDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                ).addCallback(WorkoutDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}