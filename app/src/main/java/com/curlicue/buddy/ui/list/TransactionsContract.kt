package com.curlicue.buddy.ui.list

import com.curlicue.buddy.ViewModelContract
import com.curlicue.buddy.data.TransactionDAO


interface TransactionsContract {
    interface ViewModel : ViewModelContract {
        fun onTransactionPressed(tx: TransactionDAO.Transaction?)
        fun onListScrolled(yPosition: Int)
    }
}
