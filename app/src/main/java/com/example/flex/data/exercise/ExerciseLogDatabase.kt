package com.example.flex.data.exercise

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ExerciseLog::class], version = 1, exportSchema = false)

abstract class ExerciseLogDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseLogDao

    private class ExerciseLogDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    initExerciseData(database.exerciseDao())
                }
            }
        }

        suspend fun initExerciseData(exerciseLogDao:ExerciseLogDao) {

            exerciseLogDao.insert(ExerciseLog(
                name = "Barbell back squat to box",
                rest = 90,
                reps = 8,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Barbell Full Squat",
                rest = 90,
                reps = 5,
                sets = 4,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Barbell Hip Thrust",
                rest = 90,
                reps = 5,
                sets = 4,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Romanian Deadlift With Dumbbells",
                rest = 90,
                reps = 15,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Barbell Deadlift",
                rest = 120,
                reps = 5,
                sets = 4,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Sumo Deadlift",
                rest = 120,
                reps = 6,
                sets = 4,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Single-Leg Press",
                rest = 90,
                reps = 5,
                sets = 4,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Single-leg glute bridge",
                rest = 90,
                reps = 20,
                sets = 4,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Bent Over Two-Arm Long Bar Row",
                rest = 90,
                reps = 8,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Pullups",
                rest = 90,
                reps = 6,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Pushups",
                rest = 90,
                reps = 6,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Biceps curl to shoulder press",
                rest = 90,
                reps = 8,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Dumbbell Bench Press",
                rest = 90,
                reps = 8,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Elbow plank",
                rest = 15,
                reps = 60,
                sets = 1,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Push Up to Side Plank",
                rest = 15,
                reps = 45,
                sets = 1,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Inchworm",
                rest = 10,
                reps = 45,
                sets = 1,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Jumping rope",
                rest = 10,
                reps = 90,
                sets = 1,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Broad jump",
                rest = 10,
                reps = 30,
                sets = 2,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Box jump",
                rest = 10,
                reps = 30,
                sets = 2,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Scissors Jump",
                rest = 10,
                reps = 30,
                sets = 2,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Dumbbell farmer's walk",
                rest = 10,
                reps = 60,
                sets = 2,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Dumbbell front raise to lateral raise",
                rest = 60,
                reps = 10,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Dumbbell Bench Press",
                rest = 60,
                reps = 8,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Dumbbell Flyes",
                rest = 60,
                reps = 8,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Landmine twist",
                rest = 60,
                reps = 15,
                sets = 3,
                weight = 0)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Dumbbell lunges",
                rest = 30,
                reps = 60,
                sets = 3,
                weight = 0,
                isTime = true)
            )
            exerciseLogDao.insert(ExerciseLog(
                name = "Dumbbell lunges",
                rest = 30,
                reps = 60,
                sets = 3,
                weight = 0,
                isTime = true)
            )


        }

    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ExerciseLogDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): ExerciseLogDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseLogDatabase::class.java,
                    "exercise_database"
                ).addCallback(ExerciseLogDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}