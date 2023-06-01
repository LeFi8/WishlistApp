package com.example.wishlistapp.mapServices

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
        suspend fun requestLocationName(latitude: Double, longitude: Double): String {
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
                        val addressJson = json.optJSONObject("address")
                        val amenity = addressJson?.optString("amenity")
                        val road = addressJson?.optString("road")
                        val houseNumber = addressJson?.optString("house_number")
                        val city = addressJson?.optString("city")
                        val country = addressJson?.optString("country")

                        val responseBuilder = StringBuilder()
                        if (!amenity.isNullOrEmpty())
                            responseBuilder.append("$amenity, ")

                        if (!road.isNullOrEmpty())
                            responseBuilder.append("$road, ")

                        if (!houseNumber.isNullOrEmpty())
                            responseBuilder.append("$houseNumber, ")

                        if (!city.isNullOrEmpty())
                            responseBuilder.append("$city, ")

                        if (!country.isNullOrEmpty())
                            responseBuilder.append(country)


                        response = responseBuilder.toString()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            return response
        }
    }
}