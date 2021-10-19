package com.app_devs.pushnotifications

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
const val channelName="com.app_devs.pushnotifications"

class MyFirebaseMessagingService: FirebaseMessagingService() {
    //S1-   generate the notification
    //S2-   attach the notification created with custom layout
    //S3-   show notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification!=null)
        {
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }
    }
    private fun generateNotification(title:String, message:String)
    {
        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        // channel id, channel name
        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setAutoCancel(true)
            .setVibrate((longArrayOf(1000,1000,1000,1000)))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder= builder.setContent(getRemoteView(title,message))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Android version greater or equal to oreo
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel=NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView=RemoteViews("com.app_devs.pushnotifications",R.layout.notification)
        remoteView.setTextViewText(R.id.tv_title,title)
        remoteView.setTextViewText(R.id.tv_message,message)
        remoteView.setImageViewResource(R.id.logo,R.drawable.ic_android_black_24dp)
        return remoteView

    }
}