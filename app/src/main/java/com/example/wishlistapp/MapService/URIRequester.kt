package com.example.wishlistapp.MapService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class URIRequester {
    companion object {
        suspend fun requestURI(latitude: Double, longitude: Double): String {
            val urlString =
                "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=$latitude&lon=$longitude&zoom=18&addressdetails=1"
            var response = ""

            withContext(Dispatchers.IO) {
                try {
                    val url = URL(urlString)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = connection.inputStream
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val responseString = reader.use(BufferedReader::readText)

                        val json = JSONObject(responseString)
                        response = json.getString("display_name")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            return response
        }
    }
}