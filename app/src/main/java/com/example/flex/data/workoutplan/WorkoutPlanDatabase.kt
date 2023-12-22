package com.example.flex.data.workoutplan

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flex.FlexUtil
import com.example.flex.Phase
import com.example.flex.WorkoutTypes
import com.example.flex.data.TypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [WorkoutPlan::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)

abstract class WorkoutPlanDatabase : RoomDatabase() {

    abstract fun workoutPlanDao(): WorkoutPlanDao

    private class WorkoutPlanDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            INSTANCE?.let { database ->
                scope.launch {
                    initWorkoutPlan(database.workoutPlanDao())
                }
            }
        }

        suspend fun initWorkoutPlan(workoutPlanDao: WorkoutPlanDao) {
            workoutPlanDao.insert(
                WorkoutPlan(
                    Phase.Menstrual.name,
                    listOf(
                        WorkoutTypes.Yoga.name,
                        WorkoutTypes.Walk.name,
                        WorkoutTypes.Yoga.name,
                        "Low Intensity Lift 3",
                        WorkoutTypes.Yoga.name
                    ),
                    FlexUtil.getListOfFalse(5)
                )
            )
            workoutPlanDao.insert(
                WorkoutPlan(
                    "Follicular",
                    listOf(
                        WorkoutTypes.Run.name,
                        "HIIT 1",
                        "High Intensity Lift 1",
                        WorkoutTypes.Pilates.name,
                        "HIIT 2",
                        WorkoutTypes.Run.name,
                        "High Intensity Lift 3"
                    ), FlexUtil.getListOfFalse(7)
                )
            )
            workoutPlanDao.insert(
                WorkoutPlan(
                    "Ovulatory",
                    listOf(
                        "HIIT 1",
                        "High Intensity Lift 1",
                        "HIIT 2",
                        WorkoutTypes.Run.name,
                        "High Intensity Lift 2"
                    ), FlexUtil.getListOfFalse(5)
                )
            )
            workoutPlanDao.insert(
                WorkoutPlan(
                    "Luteal",
                    listOf(
                        WorkoutTypes.Pilates.name,
                        "High Intensity Lift 3",
                        WorkoutTypes.Yoga.name,
                        "Low Intensity Lift 2",
                        WorkoutTypes.Pilates.name,
                        "High Intensity Lift 1",
                        WorkoutTypes.Run.name,
                        "Low Intensity Lift 1",
                        WorkoutTypes.Walk.name,
                        "Low Intensity Lift 3",
                        WorkoutTypes.Yoga.name,
                        WorkoutTypes.Walk.name
                    ), FlexUtil.getListOfFalse(12)
                )
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WorkoutPlanDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): WorkoutPlanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutPlanDatabase::class.java,
                    "workout_plan_database"
                ).addCallback(WorkoutPlanDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}