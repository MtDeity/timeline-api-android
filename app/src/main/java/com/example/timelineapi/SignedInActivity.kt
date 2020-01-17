package com.example.timelineapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
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

//        val tabs = findViewById<TabLayout>(R.id.tabs)
//        tabs.getTabAt(2)?.select()
        Log.i("TimelineApi", contents)
    }
}
