package com.example.timelineapi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignedInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in)

        val title = findViewById<TextView>(R.id.tvSignedInTitle)
        title.text = intent.getStringExtra("status")
        
//        val contents = File(filesDir, "token.txt").bufferedReader().useLines { lines ->
//            lines.fold("") { working, line ->
//                "$working\n$line"
//            }
//        }
//        findViewById<TextView>(R.id.tvSignedUpTitle).text = contents

    }
}
