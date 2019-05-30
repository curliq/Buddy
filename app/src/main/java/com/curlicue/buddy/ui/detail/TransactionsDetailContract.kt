package com.curlicue.buddy.ui.detail

import com.curlicue.buddy.ViewModelContract

interface TransactionsDetailContract {
    interface ViewModel: ViewModelContract {
        fun onBackPressed()
    }
}
