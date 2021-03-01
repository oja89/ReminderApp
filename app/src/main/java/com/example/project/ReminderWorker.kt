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
        MenuActivity.showNofitication(applicationContext,text!!)
        return   Result.success()
    }
}