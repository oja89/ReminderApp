package com.example.project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reminderInfo")
data class ReminderInfo(
    //message, location_x, location_y, reminder_time, creation_time, creator_id, reminder_seen
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "location_x") var location_x: Double,
    @ColumnInfo(name = "location_y") var location_y: Double,
    @ColumnInfo(name = "reminder_time") var reminder_time: String,
    @ColumnInfo(name = "creation_time") var creation_time: String,
    @ColumnInfo(name = "creator_id") var creator_id: String,
    @ColumnInfo(name = "reminder_seen") var reminder_seen: Int?
)


