package com.example.customnotificationwithcompose

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import androidx.core.app.NotificationManagerCompat
import com.example.customnotificationwithcompose.ui.theme.CustomNotificationWithComposeTheme

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

