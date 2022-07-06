package com.example.networkrequestpractice

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.networkrequestpractice.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //NetworkAsyncTask().execute("https://dummy.restapiexample.com/api/v1/employees")

        startActivity(Intent(this, NetworkActivity::class.java))
}

class NetworkAsyncTask:
    AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String?): String {
        //val url = URL("https://dummy.restapiexample.com/api/v1/employees")

        Log.d(TAG, "doInBackground: inside network async task")
        val url = URL(params[0])
        var message = ""

        try {
            val client: HttpURLConnection = url.openConnection() as HttpURLConnection
            client.connect()

            Log.d(TAG, "onCreate: connection success")
            client.requestMethod = "GET"

//            runBlocking(Dispatchers.IO) {
//                val inputStream = client.inputStream
//                readStream(inputStream)
//            }

            val inputStream = client.inputStream

            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            message = bufferedReader.readLine()

            Log.d(TAG, "doInBackground: $message")

        } //catch (e: IOException) {
//            Log.d(TAG, "onCreate: IO Exception ${e.cause}")
//        }
    catch (e: MalformedURLException) {
            Log.d(TAG, "onCreate: Malformed Exception ${e.message}")
        }

        return message
    }

//    private fun readStream(ipStream: InputStream) {
//        binding.textview.text = ipStream.toString()
//        //ipStream.toString()
//    }
}
}