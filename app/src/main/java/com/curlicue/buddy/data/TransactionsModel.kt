package com.curlicue.buddy.data

import com.curlicue.buddy.Utils
import io.reactivex.Observable


object TransactionsModel {

    fun fetchTransactions(): Observable<TransactionDAO> {
        return Utils.buildRetrofit(Endpoints::class.java).getTransactions(Utils.getBuddyAuth(), 1, 1000)
    }

}
