package com.example.timelineapi

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
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
            var result= ""
            var urlConnection: HttpURLConnection? = null

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
            }catch(e: Exception){
                e.printStackTrace()

                val errorStream = urlConnection?.errorStream;
                result = is2String(errorStream)
                errorStream?.close()
                Log.i("TimelineApiError", result)

            }finally {
                urlConnection?.disconnect()
                return result
            }
        }

        override fun onPostExecute(result: String) {
            lateinit var status: String
            var responseName = "name初期値"
            var responseBio = "bio初期値"
            var responseEmail = "email初期値"

            if (result == "") {
                status = "通信エラー"
            } else {
                val rootJSON = JSONObject(result)
                if (rootJSON.has("id")) {
                    status = "登録完了"
                    responseName = rootJSON.getString("name")
                    responseBio = rootJSON.getString("bio")
                    responseEmail = rootJSON.getString("email")
                    val responseToken = rootJSON.getString("token")
                    val tokenFile = "token.txt"
                    val tokenContent = responseToken
                    File(filesDir, tokenFile).bufferedWriter().use { writer ->
                        writer.write(tokenContent)
                    }
                } else if (rootJSON.getJSONObject("error").getJSONArray("messages")[0].toString() == "そのemailは登録されている") {
                    status = "登録済みのメールアドレスです。"
                } else if (rootJSON.getJSONObject("error").getJSONArray("messages")[0].toString().contains("blank")) {
                    status = "未入力の項目があります。"
                } else if (rootJSON.getJSONObject("error").getJSONArray("messages")[0].toString().contains("Password confirmation")) {
                    status = "確認用パスワードが一致しません。"
                } else {
                    status = "登録失敗"
                }
            }

            val intent = Intent(applicationContext, SignedUpActivity::class.java)
            intent.putExtra("status", status)
            intent.putExtra("name", responseName)
            intent.putExtra("bio", responseBio)
            intent.putExtra("email", responseEmail)
            startActivity(intent)

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
