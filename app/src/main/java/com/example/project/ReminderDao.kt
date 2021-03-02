package com.example.project.db

import androidx.room.*
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

    @Query("SELECT * FROM reminderInfo WHERE reminder_seen = :seen AND location_x == '' AND location_y == ''")
    fun getDueReminders(seen: Int): List<ReminderInfo>



    // try to get one UID
    @Query("SELECT * FROM reminderInfo WHERE uid = :id")
    fun getWithUid(id: Int): ReminderInfo

    // trying to update
    @Update
    fun updateReminder(reminderInfo: ReminderInfo)

}

