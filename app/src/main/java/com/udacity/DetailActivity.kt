package com.udacity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
private lateinit var fileNameText : TextView
private lateinit var statusText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
       Toast.makeText(applicationContext , intent.getStringExtra("title") + intent.getStringExtra("status") , Toast.LENGTH_SHORT).show()

        fileNameText = findViewById(R.id.file_name)
        statusText = findViewById(R.id.status)

        fileNameText.setText(intent.getStringExtra("title"))
        statusText.setText(intent.getStringExtra("status"))
    }

}
