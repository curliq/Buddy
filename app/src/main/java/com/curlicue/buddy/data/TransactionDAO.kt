package com.curlicue.buddy.data

import android.util.Log
import com.google.gson.annotations.SerializedName


data class TransactionDAO(
    @SerializedName("data")
    val data: Data
) {

    data class Data(
        @SerializedName("transactions")
        val transactions: List<List<Transaction>>
    )

    data class Transaction(
        @SerializedName("amount")
        val amount: String,
        @SerializedName("category_id")
        val categoryId: Number,
        @SerializedName("category_meta")
        val categoryMeta: CategoryMeta,
        @SerializedName("currency")
        val currency: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        var id: String,
        @SerializedName("product")
        val product: Product,
        @SerializedName("service")
        val service: String,
        var isFirstInDate: Boolean
    ) {
        override fun equals(other: Any?): Boolean {
            if (other !is Transaction)
                return false
            val a = (this.amount == other.amount &&
                    this.categoryId == other.categoryId &&
                    this.categoryMeta.categoryId == other.categoryMeta.categoryId &&
                    this.product.id == other.product.id &&
                    this.currency == other.currency &&
                    this.date == other.date &&
                    this.description == other.description &&
                    this.id == other.id &&
                    this.service == other.service)
            return a
        }
    }

    data class CategoryMeta(
        @SerializedName("category_id")
        val categoryId: Number,
        @SerializedName("category_name")
        val categoryName: String,
        @SerializedName("colour")
        val colour: String,
        @SerializedName("icon")
        val icon: String
    )

    data class Product(
        @SerializedName("favicon")
        val favicon: List<Favicon>,
        @SerializedName("id")
        val id: Number,
        @SerializedName("title")
        val title: String
    )

    data class Favicon(
        @SerializedName("url")
        val url: String
    )
}
