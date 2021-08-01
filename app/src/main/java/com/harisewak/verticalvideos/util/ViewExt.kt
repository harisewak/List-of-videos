package com.harisewak.verticalvideos.util

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.load(imageUrl: String) = Picasso.get().load(imageUrl).into(this)
