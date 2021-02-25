package com.example.project.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.project.ReminderInfo


@Dao
interface ReminderDao {
    @Transaction
    @Insert
    fun insert(reminderInfo: ReminderInfo): Long

    @Query("DELETE FROM reminderInfo WHERE uid = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM reminderInfo")
    fun getReminderInfos(): List<ReminderInfo>

    // try to get one UID
    @Query("SELECT * FROM reminderInfo WHERE uid = :id")
    fun getWithUid(id: Int): ReminderInfo
}

