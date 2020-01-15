package com.example.timelineapi

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var responseName: String
    lateinit var responseBio: String
    lateinit var responseEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonClick(view: View) {
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

        val receiver = SignUpReceiver()
        receiver.execute(emailStr, passwordStr, passwordConfirmationStr, nameStr, bioStr)
    }

    private inner class SignUpReceiver() : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String): String {
            val paramsValue = JSONObject()
            val email = params[0]
            val password = params[1]
            val passwordConfirmation = params[2]
            val name = params[3]
            val bio = params[4]
            paramsValue.put("name", name)
            paramsValue.put("bio", bio)
            paramsValue.put("email", email)
            paramsValue.put("password", password)
            paramsValue.put("password_confirmation", passwordConfirmation)
            val bodyParameter = JSONObject()
            bodyParameter.put("sign_up_user_params", paramsValue)

            val urlStr = "https://teachapi.herokuapp.com/sign_up"
            val url = URL(urlStr)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.addRequestProperty("Content-Type", "application/json; charset=UTF-8")
            urlConnection.doOutput = true
            urlConnection.doInput  = true
            urlConnection.connect()
            val outputStream = PrintStream(urlConnection.outputStream)
            outputStream.print(bodyParameter)
            outputStream.close()

            val inputStream = urlConnection.inputStream
            val result = is2String(inputStream)
            urlConnection.disconnect()

            return result
        }

        override fun onPostExecute(result: String) {
            val rootJSON = JSONObject(result)

            responseName = rootJSON.getString("name")
            responseBio = rootJSON.getString("bio")
            responseEmail = rootJSON.getString("email")

            val intent = Intent(applicationContext, SignedUpActivity::class.java)
            intent.putExtra("name", responseName)
            intent.putExtra("bio", responseBio)
            intent.putExtra("email", responseEmail)
            startActivity(intent)

            return
        }
    }

    private fun is2String(stream: InputStream): String {
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var line = reader.readLine()
        while (line != null) {
            sb.append(line)
            line = reader.readLine()
        }
        reader.close()
        return sb.toString()
    }
}
