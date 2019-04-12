package com.daftmobile.android4beginners5.joke

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.daftmobile.android4beginners5.SingleLiveEvent
import com.daftmobile.android4beginners5.joke.gson.GsonJokeFetcher

class JokeViewModel: ViewModel() {
    //private val jokeDataSource: JokeDataSource = JokeFetcher()
    private val jokeDataSource: JokeDataSource = GsonJokeFetcher()

    private val responseLiveData = MutableLiveData<String>()
    private val errorLiveData = SingleLiveEvent<String>()
    private val loaderVisibleData = MutableLiveData<Boolean>()  //TODO add setValue and observing in JokeActivity

    fun response(): LiveData<String> = responseLiveData
    fun error(): LiveData<String> = errorLiveData
    fun loaderVisible(): LiveData<Boolean> = loaderVisibleData

    fun call() {
        //fetchDataFromApi()
        doSomeThreading()
        //TODO cancel (first: retrofit, then: worker thread) if there is no active observer (application is closed)
        //TODO but not when is stopped (not visible) <-- background Service then?
    }

    private fun fetchDataFromApi() {
        jokeDataSource.fetch({
            //responseLiveData.setValue(it)
            responseLiveData.value = it
        }, {
            errorLiveData.value = it
        })
    }

    private fun fetchDataFromApiSynchronouslyOnWorkerThread() {
        jokeDataSource.fetchSynchronously({
            responseLiveData.postValue(it)
        }, {
            errorLiveData.postValue(it)
        })
    }

    private fun doSomeThreading() {
        println("Funny stuff from ${Thread.currentThread().name} thread")
        Thread {
            loaderVisibleData.postValue(true)   //TODO only after 0.5 seconds of waiting
            Thread.sleep(10_000)
            println("Response from ${Thread.currentThread().name} thread")
            // *post* value from worker thread
            //responseLiveData.postValue("Akuku")
            fetchDataFromApiSynchronouslyOnWorkerThread()
            loaderVisibleData.postValue(false)  //TODO only if actually shown
        }.start()
    }
}
