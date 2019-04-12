package com.daftmobile.android4beginners5.joke.gson

import android.provider.Settings
import com.daftmobile.android4beginners5.joke.JokeDataSource
import retrofit2.Callback
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException

class GsonJokeFetcher: JokeDataSource {

    private val client = OkHttpClient.Builder()
            .build()

    private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://switter.app.daftmobile.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // drugi interfejs JokeApi, taki, w którym metody zwracają Call<Joke>
    private val jokeApi = retrofit.create(GsonJokeApi::class.java)

    override fun fetch(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val call = jokeApi.getJoke(Settings.Secure.ANDROID_ID)

        call.enqueue(object : Callback<Joke> {
            override fun onFailure(call: Call<Joke>, t: Throwable) {
                onError(t.message ?: "No message")
            }

            override fun onResponse(call: Call<Joke>, response: Response<Joke>) {
                if (response.isSuccessful) {
                    //TODO check toString() (data class)
                    onSuccess(response.body()?.content ?: "Empty response")
                } else {
                    onError("Server response code: ${response.code()}")
                }
            }
        })
    }

    override fun fetchSynchronously(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        //super.fetchSynchronously(onSuccess, onError)

        val call = jokeApi.getJoke(Settings.Secure.ANDROID_ID)

        try {
            val response = call.execute()
            //val body = response.body()?.string() ?: return onError("Nic nie wróciło")
            //onSuccess(body)
            if (response.isSuccessful) {
                onSuccess(response.body()?.content ?: "Empty response")
            } else {
                onError("Server response code: ${response.code()}")
            }
        } catch (e: IOException) {
            // a problem occurred talking to the server
            onError("IOException: " + (e.message ?: "No message"))
        } catch (e: Exception) { // RuntimeException (and subclasses)
            // an unexpected error occurs creating the request or decoding the response
            onError(
                    if (e is RuntimeException) "RuntimeException: " else "" +
                    (e.message ?: "No message")
            )
        } finally {
            //nothing?
        }
    }
}
