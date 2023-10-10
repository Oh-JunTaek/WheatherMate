package com.example.wheathermate

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class WeatherCheckWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // TODO: 여기서 실제로 날씨를 확인하고 필요한 경우 알림을 보냅니다.
        // getWeatherInfo(), buildNotification() 등의 메소드가 필요합니다.

        return Result.success()
    }
}