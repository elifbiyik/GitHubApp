package com.ex.github

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide


fun ImageView.ImageLoad(url: String) {

    Glide.with(context)
        .load(url)
        .into(this)
}

/*// Adapter ile birlikte dataBinding kullanıyoruz.
@BindingAdapter("android:imageUrl")  // XML'de bu isimle kullanıcaz
fun downloadImage(view: ImageView, url: String) {
    view.ImageLoad(url)
}*/


fun ImageView.Color(colorful: Int) {
    setColorFilter(ContextCompat.getColor(context,colorful))
}


fun Fragment.replace(fragment : Fragment){

    requireActivity().supportFragmentManager.beginTransaction()
        .replace(R.id.constraint, fragment)
        .addToBackStack(null)
        .commit()
}


