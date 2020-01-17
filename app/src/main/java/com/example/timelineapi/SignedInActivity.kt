package com.example.timelineapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class SignedInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in)

        val contents = File(filesDir, "token.txt").bufferedReader().useLines { lines ->
            lines.fold("") { working, line ->
                "$working\n$line"
            }
        }

        Log.i("TimelineApi", contents)
    }
}
