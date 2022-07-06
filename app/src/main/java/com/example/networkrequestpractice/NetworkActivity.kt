package com.example.networkrequestpractice

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.networkrequestpractice.databinding.ActivityNetworkBinding
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class NetworkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNetworkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        runBlocking {
//            performNetworkRequest()
//        }

        performNetworkRequest1()

        Log.d(TAG, "performNetworkRequest: outside run blocking")

    }

    private suspend fun performNetworkRequest(){
        coroutineScope{

            launch(Dispatchers.IO) {
                val url = URL("https://reqres.in/api/users?page=2")
                val httpUrlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection


                try {
                    val responseCode = httpUrlConnection.responseCode

                    Log.d(TAG, "performNetworkRequest: response code: $responseCode")
                    if(responseCode != 200){
                        throw IOException("The error from server is $responseCode")
                    }

                    val bufferedReader = BufferedReader(
                        InputStreamReader(
                            httpUrlConnection.inputStream
                        )
                    )

                    var responseString = ""

                    while(true){
                        val line = bufferedReader.readLine() ?: break
                        responseString += line
                    }

                    Log.d(TAG, "performNetworkRequest: $responseString")
                    val userProfileResponse = Gson().fromJson(responseString, UserProfileResponse::class.java)

                    Log.d(TAG, "performNetworkRequest: data ${userProfileResponse.data[0].firstName}")
//                    withContext(Dispatchers.Main){
//                        Log.d(TAG, "performNetworkRequest: inside main thread")
//                        binding.textView.text  = userProfileResponse.data[0].let {
//                            "${it.firstName} + ${it.lastName}"
//                        }
//                    }
                    
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "performNetworkRequest: inside main")
                    }
                }
                catch (e: Exception){
                    Log.d(TAG, "performNetworkRequest: exception ${e.message}")
                }
                finally {
                    httpUrlConnection.disconnect()
                }

            }

            Log.d(TAG, "performNetworkRequest: outside launch")
            }
        Log.d(TAG, "performNetworkRequest: outside coroutine scope ")

    }

    private  fun performNetworkRequest1(){
        CoroutineScope(Dispatchers.IO).launch{


                val url = URL("https://reqres.in/api/users?page=2")
                val httpUrlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection


                try {
                    val responseCode = httpUrlConnection.responseCode

                    Log.d(TAG, "performNetworkRequest: response code: $responseCode")
                    if(responseCode != 200){
                        throw IOException("The error from server is $responseCode")
                    }

                    val bufferedReader = BufferedReader(
                        InputStreamReader(
                            httpUrlConnection.inputStream
                        )
                    )

                    var responseString = ""

                    while(true){
                        val line = bufferedReader.readLine() ?: break
                        responseString += line
                    }

                    Log.d(TAG, "performNetworkRequest: $responseString")

//                    val userProfileResponse = Gson().fromJson(responseString, UserProfileResponse::class.java)

                    val userProfileResponse = parseJsonStringToObject(responseString)

                    //val userProfileResponse =
                    Log.d(TAG, "performNetworkRequest: data ${userProfileResponse.currentPage}")

                    withContext(Dispatchers.Main){
                        Log.d(TAG, "performNetworkRequest: inside main")
                        binding.textView.text  = userProfileResponse.data[0].let {
                            "${it.id}  ${it.lastName}"
                        }
                    }
                }
                catch (e: Exception){
                    Log.d(TAG, "performNetworkRequest: exception ${e.message}")
                }
                finally {
                    httpUrlConnection.disconnect()
                }

            }

            Log.d(TAG, "performNetworkRequest: outside launch")
        }

    //With the JSONTokener, we can parse a JSON string into an object.
    private fun parseJsonStringToObject(jsonString: String): UserProfileResponse{

        val userEntities = mutableListOf<UserEntity>()

        val jsonObject = JSONTokener(jsonString).nextValue() as JSONObject

        val page = jsonObject.getString("page")
        val perPage = jsonObject.getString("per_page")
        val total = jsonObject.getString("total")
        val totalPages = jsonObject.getString("total_pages")

        val dataList = jsonObject.getJSONArray("data")
        for(i in 0 until dataList.length()){
            val data = dataList.getJSONObject(i)

            userEntities.add(
                UserEntity(
                data.getString("id"),
                    data.getString("first_name"),
                    data.getString("last_name"),
                    data.getString("email"),
                    data.getString("avatar")
            )
            )

        }

        return UserProfileResponse(
            page,
            perPage,
            total,
            totalPages,
            userEntities
        )

    }


}

// Works
//Main function
//calling networkfunction
//
//fun networkfunction
//CoroutineScope(Dispatchers.IO).launch{
//}


// Not working
//Main function
// runBlocking{
//calling suspend function networkFunction
//}
//
//suspend fun networkFunction(){
//    coroutineScope {
//        launch {
//
//        }
//    }
//}
