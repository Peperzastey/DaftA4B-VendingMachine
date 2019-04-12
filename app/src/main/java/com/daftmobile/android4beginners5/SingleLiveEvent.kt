package com.daftmobile.android4beginners5

import android.support.annotation.MainThread
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Konrad Kowalewski.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            Log.d(TAG, "internal MutableLiveData's observer called")
            if (pending.compareAndSet(true, false)) {
                Log.d(TAG, "SingleLiveEvent's observer called")
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        Log.d(TAG, "SingleLiveData's setValue called => its observer will be called once")
        pending.set(true)
        super.setValue(t)
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}
