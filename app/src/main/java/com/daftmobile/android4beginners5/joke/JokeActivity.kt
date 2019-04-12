package com.daftmobile.android4beginners5.joke

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.daftmobile.android4beginners5.R
import kotlinx.android.synthetic.main.activity_joke.*

private const val TAG = "JokeActivity"

class JokeActivity : AppCompatActivity() {

    private val jokeViewModel: JokeViewModel by lazy { ViewModelProviders.of(this).get(JokeViewModel::class.java) }

    private var dialogRef: DialogInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joke)
        observeResponses()
        observeErrors()
        observeLoader()

        //TODO block (disable / AND! do nothing) this button until LiveData is updated
        callButton.setOnClickListener {
            jokeViewModel.call()

            Thread {
                val timeMillis = 5_000L
                Thread.sleep(timeMillis)
                Log.i(TAG, "Worker thread woke up after ${timeMillis/1000.0} seconds")
            }.start()
        }
    }

    private fun observeResponses() {
        jokeViewModel.response().observe(this, Observer {
            jokeView.text = it
        })
    }

    private fun observeErrors() {
        jokeViewModel.error().observe(this, Observer {
            dialogRef =
                AlertDialog.Builder(this)
                    .setTitle(R.string.error)
                    .setMessage(it)
                    .show()
        })
    }

    private fun observeLoader() {
        jokeViewModel.loaderVisible().observe(this, Observer {
            loader.visibility = if (it == true) View.VISIBLE else View.GONE
        })
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        dialogRef?.dismiss()
        dialogRef = null
        super.onStop()
    }
}
