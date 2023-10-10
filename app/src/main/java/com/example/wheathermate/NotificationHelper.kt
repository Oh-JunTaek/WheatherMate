package com.example.wheathermate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "com.example.wheathermate"
        private const val CHANNEL_NAME = "WeatherMate Channel"
    }

    // 알림 채널 생성 (안드로이드 Oreo 이상에서 필요)
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            getManager().createNotificationChannel(channel)
        }
    }

    // NotificationManager 인스턴스 반환
    private fun getManager(): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    // 알림 생성 및 반환
    fun buildNotification(title: String, content: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_wmicon_background)  // 아이콘 설정
            .setContentTitle(title)  // 제목 설정
            .setContentText(content)  // 내용 설정
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 우선순위 설정
            .setAutoCancel(true)  // 사용자가 터치하면 자동으로 사라지게 설정
    }

    // 실제로 알림 보내기
    fun notify(id: Int, notificationBuilder: NotificationCompat.Builder){
        getManager().notify(id, notificationBuilder.build())
    }
}