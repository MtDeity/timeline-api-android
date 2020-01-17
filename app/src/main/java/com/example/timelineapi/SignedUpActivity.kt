package com.example.timelineapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.io.File

class SignedUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_up)

        val email = findViewById<TextView>(R.id.tvSignedUpEmailDescription)
        val name = findViewById<TextView>(R.id.tvSignedUpNameDescription)
        val bio = findViewById<TextView>(R.id.tvSignedUpBioDescription)

        if (intent.getStringExtra("status") == "登録完了") {
            email.text = intent.getStringExtra("email")
            name.text = intent.getStringExtra("name")
            bio.text = intent.getStringExtra("bio")
        } else {
            val title = findViewById<TextView>(R.id.tvSignedUpTitle)
            title.text = intent.getStringExtra("status")
            val emailTitle = findViewById<TextView>(R.id.tvSignedUpEmail)
            val nameTitle = findViewById<TextView>(R.id.tvSignedUpName)
            val bioTitle = findViewById<TextView>(R.id.tvSignedUpBio)
            emailTitle.visibility = View.GONE
            email.visibility = View.GONE
            nameTitle.visibility = View.GONE
            name.visibility = View.GONE
            bioTitle.visibility = View.GONE
            bio.visibility = View.GONE
        }


//        val contents = File(filesDir, "token.txt").bufferedReader().useLines { lines ->
//            lines.fold("") { working, line ->
//                "$working\n$line"
//            }
//        }
//        findViewById<TextView>(R.id.tvSignedUpTitle).text = contents

    }
}
