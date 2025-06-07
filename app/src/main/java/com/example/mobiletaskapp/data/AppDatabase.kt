package com.example.mobiletaskapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class, Priority::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}