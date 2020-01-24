package com.example.timelineapi

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonClick(view: View) {
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)

        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        val receiver = SignUpReceiver()
        receiver.execute(emailStr, passwordStr)
    }

    fun onLinkClick(view: View) {
        val intent = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(intent)
    }

    private inner class SignUpReceiver() : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String): String {
            var result= ""
            var urlConnection: HttpURLConnection? = null

            val paramsValue = JSONObject()
            val email = params[0]
            val password = params[1]
            paramsValue.put("email", email)
            paramsValue.put("password", password)
            val bodyParameter = JSONObject()
            bodyParameter.put("sign_in_user_params", paramsValue)

            val urlStr = "https://teachapi.herokuapp.com/sign_in"
            val url = URL(urlStr)

            try{
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 100000
                urlConnection.readTimeout = 100000
                urlConnection.requestMethod = "POST"
                urlConnection.addRequestProperty("Content-Type", "application/json; charset=UTF-8")
                urlConnection.doOutput = true
                urlConnection.doInput = true
                urlConnection.connect()
                val outputStream = PrintStream(urlConnection.outputStream)
                outputStream.print(bodyParameter)
                outputStream.close()

                val inputStream = urlConnection.inputStream
                result = is2String(inputStream)
                inputStream.close()
                Log.i("TimelineApiSuccess", result)
            }catch(e: Exception){
                e.printStackTrace()

                val errorStream = urlConnection?.errorStream
                result = is2String(errorStream)
                errorStream?.close()
                Log.i("TimelineApiError", result)
            }finally {
                urlConnection?.disconnect()
                return result
            }
        }

        override fun onPostExecute(result: String) {
            val error = findViewById<TextView>(R.id.tvErrorMessage)

            if (result == "") {
                error.text = "通信エラー"
            } else {
                val rootJSON = JSONObject(result)
                if (rootJSON.has("id")) {
                    val responseToken = rootJSON.getString("token")
                    val tokenFile = "token.txt"
                    File(filesDir, tokenFile).bufferedWriter().use { writer ->
                        writer.write(responseToken)
                    }
                    val intent = Intent(applicationContext, SignedInActivity::class.java)
                    startActivity(intent)
                } else if (rootJSON.getJSONObject("error").getJSONArray("messages")[0].toString() == "そのemailはありません") {
                    error.text = "登録のないメールアドレスです。"
                } else if (rootJSON.getJSONObject("error").getJSONArray("messages")[0].toString() == "そのemailもしくわpasswordが違います") {
                    error.text = "メールアドレスもしくはパスワードが間違っています。"
                } else {
                    error.text = "ログイン失敗"
                }
            }
            return
        }
    }

    private fun is2String(stream: InputStream?): String {
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

