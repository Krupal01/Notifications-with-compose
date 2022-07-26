package com.example.customnotificationwithcompose

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.customnotificationwithcompose.ui.theme.CustomNotificationWithComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val channelId = "my channel"
            LaunchedEffect(key1 = channelId){
                createNotificationChannel(context, channelId)
            }
            CustomNotificationWithComposeTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally ,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { simpleNotification(context, channelId)}) {
                        Text(text = "show simple notification")
                    }

                    Button(onClick = { actionNotification(context, channelId)}) {
                        Text(text = "show action notification")
                    }

                    Button(onClick = { progressbarNotification(context, channelId)}) {
                        Text(text = "show progress notification")
                    }
                }
            }
        }
    }

    private fun simpleNotification(context: Context, channelId: String){
        val notificationBuilder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("hello world")
            .setContentText("hello world")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            notify(0, notificationBuilder.build())
        }
    }

    private fun actionNotification(context: Context , channelId: String){

        val intent = Intent(context,MyReceiver::class.java).apply{
            action = "com.notification.dummy"
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0)

        val notificationBuilder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("hello world")
            .setContentText("hello world")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_launcher_background,"action",pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(1, notificationBuilder.build())
        }
    }

    private fun progressbarNotification(context: Context,channelId: String){
        val builder = NotificationCompat.Builder(this, channelId).apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(R.drawable.ic_launcher_background)
            priority = NotificationCompat.PRIORITY_LOW
        }
        val PROGRESS_MAX = 100
        var PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            notify(0, builder.build())

            for (i in 1..100){
                if (i == 100){
                    builder.setContentText("Download complete")
                        .setProgress(0, 0, false)
                    notify(0, builder.build())
                }
                lifecycleScope.launch {
                    delay(500)
                    PROGRESS_CURRENT = i
                    builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                    notify(0, builder.build())
                }
            }
        }
    }

    private fun createNotificationChannel(context: Context, channelId : String){
        val channelName = "my channel"
        val channelDescription = "my channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val myChannel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = channelDescription
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(myChannel)
        }
    }
}

