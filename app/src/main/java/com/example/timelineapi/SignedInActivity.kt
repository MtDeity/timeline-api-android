package com.example.timelineapi

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class SignedInActivity : AppCompatActivity() {
    val list = mutableListOf<Post>()
    private lateinit var recycleViewAdapter: RecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in)

        val token = File(filesDir, "token.txt").bufferedReader().useLines { lines ->
            lines.fold("") { working, line ->
                line
            }
        }
        val page = ""
        val limit = ""
        val query = ""

        val receiver = ListReceiver()
        receiver.execute(token, page, limit, query)
    }

    private inner class ListReceiver : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String): String {
            val token = params[0]
            val page = params[1]
            val limit = params[2]
            val query = params[3]

            val queryParams: HashMap<String, String> = HashMap()
            queryParams.put("page", page)
            queryParams.put("limit", limit)
            queryParams.put("query", query)

            var result= ""
            var urlConnection: HttpURLConnection? = null

            val urlStr = "https://teachapi.herokuapp.com/posts"
            val builder = Uri.Builder()
            val keys: Set<String> = queryParams.keys
            for (key in keys) {
                builder.appendQueryParameter(key, queryParams[key])
            }
            val url = URL(urlStr + builder.toString())

            try{
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 100000
                urlConnection.readTimeout = 100000
                urlConnection.requestMethod = "GET"
                urlConnection.addRequestProperty("Content-Type", "application/json; charset=UTF-8")
                urlConnection.addRequestProperty("Authorization", "Bearer $token")
                urlConnection.doOutput = false
                urlConnection.doInput = true
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                result = is2String(inputStream)
                inputStream.close()
            }catch(e: Exception){
                e.printStackTrace()

                val errorStream = urlConnection?.errorStream
                result = is2String(errorStream)
                errorStream?.close()
                Log.i("SignedInActivity", result)
            }finally {
                urlConnection?.disconnect()
                return result
            }
        }

        override fun onPostExecute(result: String) {
            if (result == "") {
                Log.i("SignedInActivity", "通信エラー")
            } else {
                try {
                    val rootJSON = JSONArray(result)
                    for (i in 0 until rootJSON.length()) {
                        val post = rootJSON.getJSONObject(i)
                        val userId = post.getJSONObject("user").getInt("id")
                        val userName = post.getJSONObject("user").getString("name")
                        val text = post.getString("text")
                        val postedAt = post.getString("created_at")

                        list.add(Post(userId, userName, text, postedAt))
                    }

                    findViewById<RecyclerView>(R.id.recycler_view).also { recyclerView: RecyclerView ->
                        recyclerView.adapter = RecycleViewAdapter(list)
                        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                    Log.i("SignedInActivity", "エラー")
                }
            }
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
