package com.alexluque.android.mymusicapp.mainactivity.controller.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.alexluque.android.mymusicapp.mainactivity.controller.Event

fun <T : Any> LifecycleOwner.runIfNotHandled(liveData: LiveData<Event<T>>, block: (T) -> Unit) =
    liveData.observe(this, Observer { event ->
        event.getContentIfNotHandled()?.let { block(it) }
    })