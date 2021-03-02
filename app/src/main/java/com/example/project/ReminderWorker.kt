package com.example.project

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext,workerParameters) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val text = inputData.getString("message") // this comes from the reminder parameters
        val uid = inputData.getInt("uid", 0)
        MenuActivity.showNofitication(applicationContext,text!!,uid)
        return   Result.success()
    }
}