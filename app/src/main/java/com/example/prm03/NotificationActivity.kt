package com.example.prm03

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import java.lang.Object
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent.getActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_favorites.*

//Class implementing the notifications possibility
class NotificationActivity: AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.notificationexample"
    private val description = "Test notification"

    //When the notifications button in the favorites page is pressed
    //The notifications are turned on and a nice pop up will appear
    //with a message showcasing this, build with the variables declared above
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

     //   public fun sendNotification() { }

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            FavoritesActivity().NotBtn.setOnClickListener()
            {
                val intent = Intent(this, FeedActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                val contentView = RemoteViews("prm03", R.layout.notification_layout)
                contentView.setTextViewText(R.id.tv_title, "CodeAndroid")
                contentView.setTextViewText(R.id.tv_content, "Text notification")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(
                        channelId,
                        description,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(this, channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                this.resources,
                                R.drawable.ic_launcher_foreground
                            )
                        )
                        .setContentIntent(pendingIntent)
                } else {

                    builder = Notification.Builder(this)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_foreground
                            )
                        )
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(1234, builder.build())
            }
        }
  /*
        private fun displayNotification() {

            var builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notification")
                .setContentText("this is your first notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            var notification: NotificationManagerCompat = NotificationManagerCompat.from(this)
            notification.notify(123, builder.build())

        } */
}

