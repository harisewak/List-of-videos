package com.harisewak.verticalvideos.util

import android.view.View
import android.widget.ImageView
import com.harisewak.verticalvideos.BASE_URL
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

fun ImageView.load(imageUrl: String) = Picasso.get().load(BASE_URL + imageUrl).into(this)

fun ImageView.loadWithCallback(imageUrl: String) =
    Picasso.get().load(BASE_URL + imageUrl).into(this, object : Callback {
        override fun onSuccess() {
            logd("onSuccess")
        }

        override fun onError(e: Exception?) {
            logd("onError: $e")
        }

    })

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
