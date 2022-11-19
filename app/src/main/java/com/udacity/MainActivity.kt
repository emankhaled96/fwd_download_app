package com.udacity

import android.app.DownloadManager
import android.app.DownloadManager.STATUS_RUNNING
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private val NOTIFICATION_ID = 0

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton: RadioButton
    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var cursor: Cursor
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var title: String? = null
    private var status: String? = null
    private var downloaded: Long? = null
    private var totalFileSize: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        custom_button.buttonState = ButtonState.Loading
        createChannel(CHANNEL_ID , "download channel")

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        radioGroup = findViewById(R.id.radio_group)
        custom_button.setOnClickListener {

            if (radioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                custom_button.buttonState = ButtonState.Clicked
                download()

            }
        }
        radioGroup.setOnCheckedChangeListener { _, isChecked ->
            radioButton = findViewById(isChecked)
            radioButton.setOnClickListener {
                if (radioButton.text.equals(getString(R.string.glide))) {
                    URL = "https://github.com/bumptech/glide"
                } else if (radioButton.text.equals(getString(R.string.current))) {
                    URL =
                        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"

                } else {
                    URL = "https://github.com/square/retrofit"

                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)



            cursor = downloadManager.query(id?.let { DownloadManager.Query().setFilterById(it) })

            if (cursor.moveToFirst()) {

                val statusId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                title = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE))
                totalFileSize =
                    cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                if (statusId == DownloadManager.STATUS_SUCCESSFUL) {
                    status = "Success"
                } else if (statusId == DownloadManager.STATUS_FAILED) {
                    status = "Failed"
                }
            }

            notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.sendNotification(
                context.getText(R.string.notification_description).toString(),
                context
            )
            custom_button.buttonState = ButtonState.Completed
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                    .apply {
                        setShowBadge(false)
                    }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download Completed"
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }


    }

    fun NotificationManager.sendNotification(message: String, context: Context) {
        val contentIntent = Intent(context, DetailActivity::class.java)
            .putExtra("title", title)
            .putExtra("status", status)
        pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        action = NotificationCompat.Action(
            R.drawable.ic_assistant_black_24dp,
            context.getString(R.string.notification_button), pendingIntent
        )
        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.notification_channel)
        )

            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .addAction(action)
            .setAutoCancel(true)
        notify(NOTIFICATION_ID, builder.build())
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(radioButton.text)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }


    companion object {
        private var URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    override fun onResume() {
        super.onResume()
        custom_button.buttonState = ButtonState.Loading
    }

}
