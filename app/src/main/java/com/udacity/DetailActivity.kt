package com.udacity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var fileNameText: TextView
    private lateinit var statusText: TextView
    private lateinit var okBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        fileNameText = findViewById(R.id.file_name)
        statusText = findViewById(R.id.status)
        okBtn = findViewById(R.id.ok_button)
        fileNameText.setText(intent.getStringExtra("title"))
        statusText.setText(intent.getStringExtra("status"))
        okBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }

    }

}
