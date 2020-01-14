package com.example.timelineapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btSignUp)
        val listenr = ButtonClickListener()
        button.setOnClickListener(listenr)
    }

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val email = findViewById<EditText>(R.id.etEmail)
            val password = findViewById<EditText>(R.id.etPassword)
            val passwordConfirmation = findViewById<EditText>(R.id.etPasswordConfirmation)
            val name = findViewById<EditText>(R.id.etName)
            val bio = findViewById<EditText>(R.id.etBio)

            val emailStr = email.text.toString()
            val passwordStr = password.text.toString()
            val passwordConfirmationStr = passwordConfirmation.text.toString()
            val nameStr = name.text.toString()
            val bioStr = bio.text.toString()

            val button = findViewById<Button>(R.id.btSignUp)
            button.setText("登録完了")
        }
    }
}
