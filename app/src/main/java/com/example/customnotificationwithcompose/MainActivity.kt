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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.customnotificationwithcompose.ui.theme.CustomNotificationWithComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomNotificationWithComposeTheme {
                MainNavHost()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val channelId = "my channel"
    LaunchedEffect(key1 = channelId){
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
    fun simpleNotification(context: Context, channelId: String){
        val notificationBuilder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("hello world")
            .setContentText("hello world")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            notify(0, notificationBuilder.build())
        }
    }

    fun actionNotification(context: Context , channelId: String){

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

    fun progressbarNotification(context: Context,channelId: String){
        val builder = NotificationCompat.Builder(context, channelId).apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(R.drawable.ic_launcher_background)
            priority = NotificationCompat.PRIORITY_LOW
        }
        val PROGRESS_MAX = 100
        var PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(context).apply {
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            notify(0, builder.build())

            for (i in 1..100){
                if (i == 100){
                    builder.setContentText("Download complete")
                        .setProgress(0, 0, false)
                    notify(0, builder.build())
                }
                lifecycleOwner.lifecycleScope.launch {
                    delay(500)
                    PROGRESS_CURRENT = i
                    builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                    notify(0, builder.build())
                }
            }
        }
    }

    fun navigateToComposable(context: Context,channelId: String){
        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "app://notification.com/notification".toUri()
        )

        val pending: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationBuilder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("hello world")
            .setContentText("hello world")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pending)

        with(NotificationManagerCompat.from(context)) {
            notify(0, notificationBuilder.build())
        }
    }

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

        Button(onClick = { navigateToComposable(context, channelId)}) {
            Text(text = "navigate to composable notification")
        }
    }
}

@Composable
fun NotificationScreen() {
    Text(text = "Notification Screen")
}
