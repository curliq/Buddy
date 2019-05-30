package com.curlicue.buddy

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.picasso.Picasso
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Utils {
    const val API_BASE_URL = "http://35.230.147.152"

    fun <T : Any> buildRetrofit(service: Class<T>): T {
        val builder = OkHttpClient().newBuilder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(builder.build())
            .build()
            .create(service)
    }

    fun jsonToString(obj: Any?): String {
        return Gson().toJson(obj)
    }

    inline fun <reified T : Any?> stringToJson(obj: String?): T? {
        return try {
            Gson().fromJson(obj, T::class.java) as T
        } catch (e: Exception) {
            null
        }
    }

    fun ImageView.loadImageUrl(url: String, placeholderRes: Int) {
        Picasso.get().load(url).placeholder(placeholderRes).into(this)
    }

    fun String.toDate(format: String = "yyyy-MM-dd"): DateTime {
        return DateTimeFormat.forPattern(format).parseDateTime(this)
    }

    fun DateTime.toReadable(): String {
        val daySuffix = when (if (this.dayOfMonth < 20) this.dayOfMonth else this.dayOfMonth % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
        return "${this.toString("EEEE")} ${this.dayOfMonth}$daySuffix ${this.toString("MMMM")}"
    }

    fun makeOblong(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(color)
            cornerRadius = 10000f
        }
    }

    fun getBuddyAuth(): String {
        return "bwt ${BuildConfig.BUDDY_API_TOKEN}"
    }

}
