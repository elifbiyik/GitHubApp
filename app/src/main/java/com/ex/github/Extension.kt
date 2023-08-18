package com.ex.github

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


fun ImageView.ImageLoad(url: String) {

    Glide.with(context)
        .load(url)
        .into(this)
}

// Adapter ile birlikte dataBinding kullanıyoruz.
@BindingAdapter("android:imageUrl")  // XML'de bu isimle kullanıcaz
fun downloadImage(view: ImageView, url: String) {
    view.ImageLoad(url)
}


