@file:Suppress("UNUSED")

package com.daftmobile.android4beginners5.joke.gson

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface GsonJokeApi {

    @GET("/api/hello")
    fun getHello(): Call<ResponseBody>  //TODO? Call<Joke> ?

    // null as parameter annotated with Header means no response header
    @GET("/api/joke")
    fun getJoke(@Header("x-device-uuid") token: String? = null): Call<Joke>
}