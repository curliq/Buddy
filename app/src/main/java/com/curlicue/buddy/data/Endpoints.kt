package com.curlicue.buddy.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Endpoints {

    @GET("transactions")
    fun getTransactions(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Observable<TransactionDAO>

}
