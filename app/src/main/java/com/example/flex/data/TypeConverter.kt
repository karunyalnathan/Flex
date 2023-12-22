package com.example.flex.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.lang.reflect.Type

class TypeConverter {
        @TypeConverter
        fun fromString(value: String): List<String> {
            val listType: Type = object : com.google.gson.reflect.TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayListToString(list: List<String>): String {
            val gson = Gson()
            return gson.toJson(list)
        }


    @TypeConverter
    fun fromListBoolean(list: List<Boolean>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toListBoolean(data: String): List<Boolean> {
        return listOf(*data.split(",").map { it.toBoolean() }.toTypedArray())
    }
}