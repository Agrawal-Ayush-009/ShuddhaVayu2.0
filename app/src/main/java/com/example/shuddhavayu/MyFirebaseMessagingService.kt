package com.example.shuddhavayu

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId="notification_channel"
const val channelName="com.example.shuddhavayu"

class MyFirebaseMessagingService: FirebaseMessagingService() {
    fun generateNotification(title:String,description:String){
        val intent= Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder: NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,
            channelId).setSmallIcon(R.drawable.splashscreen_icon).setAutoCancel(true).setVibrate(
            longArrayOf(1000,1000,1000,1000)).setOnlyAlertOnce(true).setContentIntent(pendingIntent)

        builder.setContent(getRemoteView(title, description))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel= NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }

    private fun getRemoteView(title: String, description: String): RemoteViews? {
        val remoteView=RemoteViews("com.example.shuddhavayu",R.layout.notification)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.description,description)
        remoteView.setImageViewResource(R.id.image,R.drawable.splashscreen_icon)
        return remoteView

    }
    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }

    }
}