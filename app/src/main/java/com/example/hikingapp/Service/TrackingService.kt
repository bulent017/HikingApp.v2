package com.example.hikingapp.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.hikingapp.Constants.ACTION_PAUSE_SERVICE
import com.example.hikingapp.Constants.ACTION_START_OR_RESUME_SERVICE
import timber.log.Timber

class TrackingService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    Timber.d("Started or resumed service")
                }
                ACTION_PAUSE_SERVICE -> {

                }
                else -> {}
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }


}
