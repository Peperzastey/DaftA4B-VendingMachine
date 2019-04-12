package com.daftmobile.android4beginners5.joke

interface JokeDataSource {

    fun fetch(onSuccess: (String) -> Unit, onError: (String) -> Unit)
    fun fetchSynchronously(onSuccess: (String) -> Unit, onError: (String) -> Unit) = Unit
}
