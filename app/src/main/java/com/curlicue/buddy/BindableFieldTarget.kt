package com.curlicue.buddy

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class BindableFieldTarget(
    private val observableField: MutableLiveData<Drawable>,
    private val resources: Resources
) : Target {

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        observableField.value = errorDrawable
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
        observableField.value = BitmapDrawable(resources, bitmap)
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable) {
        observableField.value = placeHolderDrawable
    }

}
