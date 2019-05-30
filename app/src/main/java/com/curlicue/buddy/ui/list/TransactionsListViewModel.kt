package com.curlicue.buddy.ui.list

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.curlicue.buddy.data.TransactionDAO
import com.curlicue.buddy.data.TransactionsModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.curlicue.buddy.Utils.toDate


class TransactionsListViewModel : ViewModel(), TransactionsContract.ViewModel {

    private val _mainLoadingVisibility = MutableLiveData<Int>()
    val mainLoadingVisibility: LiveData<Int>
        get() = _mainLoadingVisibility

    private val _backgroundLoadingVisibility = MutableLiveData<Int>()
    val backgroundLoadingVisibility: LiveData<Int>
        get() = _backgroundLoadingVisibility

    private val _toolbarElevation = MutableLiveData<Int>()
    val toolbarElevation: LiveData<Int>
        get() = _toolbarElevation

    private val _transactionPressed = MutableLiveData<TransactionDAO.Transaction>()
    val transactionPressed: LiveData<TransactionDAO.Transaction>
        get() = _transactionPressed

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = Transformations.map(_errorMessage) { "Error getting transactions: $it" }

    val errorMessageVisibility: LiveData<Int>
        get() = Transformations.map(_errorMessage) { if (it != null) View.VISIBLE else View.GONE }

    private lateinit var subscription: Disposable
    private var _transactions = MutableLiveData<List<TransactionDAO.Transaction>>()
    val transactions: LiveData<List<TransactionDAO.Transaction>>
        get() = _transactions

    fun start(): TransactionsListViewModel {
        _toolbarElevation.value = 0
        _mainLoadingVisibility.value = if (_transactions.value?.isEmpty() != false) View.VISIBLE else View.GONE
        _backgroundLoadingVisibility.value = if (_transactions.value?.isEmpty() == false) View.VISIBLE else View.GONE
        subscription = TransactionsModel.fetchTransactions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate {
                _mainLoadingVisibility.value = View.GONE
                _backgroundLoadingVisibility.value = View.GONE
            }
            .subscribe(
                {
                    val mapped = ArrayList<TransactionDAO.Transaction>()
                    it.data.transactions.forEach { items -> mapped.addAll(items) }

                    // set a flag on each item to display a date divider, only for the first items in each day
                    (0 until mapped.size).forEach { i ->
                        if (i == 0 || mapped[i].date.toDate().dayOfYear() != mapped[i - 1].date.toDate().dayOfYear())
                            mapped[i].isFirstInDate = true

                        // also override the id for each transaction, transaction IDs from the backend are repeated
                        mapped[i].id = "TX$i"
                    }

                    var hasDifferentItems = false
                    mapped.forEach {
                        if (it != _transactions.value?.first { tx -> tx.id == it.id }) {
                            Log.i("tagg", "has diff: ")
                            Log.i("tagg", it.toString())
                            hasDifferentItems = true
                        }
                    }
                    if (hasDifferentItems)
                        _transactions.value = mapped
                },
                { _errorMessage.value = it.message }
            )
        return this
    }

    override fun onTransactionPressed(tx: TransactionDAO.Transaction?) {
        _transactionPressed.value = tx
    }

    override fun onListScrolled(yPosition: Int) {
        val _yPosition = yPosition / 10

        if (_toolbarElevation.value!! <= 12) {
            if (_yPosition <= 12)
                _toolbarElevation.value = _yPosition
            else
                _toolbarElevation.value = 12
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}
