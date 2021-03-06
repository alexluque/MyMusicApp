package com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun ImageView.loadImage(url: String) {
    val view = this
    GlobalScope.launch(Dispatchers.Main) {
        Picasso.get()
            .load(url)
            .error(R.drawable.ic_launcher_background)
            .into(view)
    }
}

fun View.makeLongSnackbar(msg: String) =
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
        .setTextColor(ContextCompat.getColor(this.context, R.color.colorSecondaryText))
        .show()

fun <T : Any> RecyclerView.Adapter<*>.updateData(dataSet: MutableList<Any>, newData: List<T>) {
    dataSet.clear()
    dataSet.addAll(newData)
    this.notifyDataSetChanged()
}

fun View.showKeyboard(activity: Activity) {
    this.requestFocus()
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}