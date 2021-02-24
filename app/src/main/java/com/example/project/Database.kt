package com.example.project.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project.ReminderInfo
import com.example.project.db.ReminderDao

@Database(entities = arrayOf(ReminderInfo::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}