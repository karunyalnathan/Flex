package com.example.flex

object FlexUtil {
     fun getListOfFalse(size: Int): MutableList<Boolean> {
        return mutableListOf<Boolean>().apply {
            for (i in 1..size) add(false)
        }
    }
}