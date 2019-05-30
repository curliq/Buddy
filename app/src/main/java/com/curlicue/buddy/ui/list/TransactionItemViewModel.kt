package com.curlicue.buddy.ui.list

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.curlicue.buddy.Application
import com.curlicue.buddy.R
import com.curlicue.buddy.Utils.toDate
import com.curlicue.buddy.Utils.toReadable
import com.curlicue.buddy.data.TransactionDAO
import org.joda.time.DateTime
import java.util.*


class TransactionItemViewModel(private val tx: TransactionDAO.Transaction) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _label = MutableLiveData<String>()
    val label: LiveData<String>
        get() = _label

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount

    private val _amountColor = MutableLiveData<Int>()
    val amountColor: LiveData<Int>
        get() = _amountColor

    private val _currency = MutableLiveData<String>()
    val currency: LiveData<String>
        get() = _currency

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image
    var imagePlaceholder: Int = 0

    private val _dateDivider = MutableLiveData<String>()
    val dateDivider: LiveData<String>
        get() = _dateDivider

    private val _dateDividerVisibility = MutableLiveData<Int>()
    val dateDividerVisibility: LiveData<Int>
        get() = _dateDividerVisibility

    fun start(): TransactionItemViewModel {
        _name.value = tx.product.title
        _currency.value = Currency.getInstance(tx.currency).symbol
        _amount.value = if (tx.amount[0] == '-') tx.amount.replace("-", "") else "+ ${tx.amount}"
        _amountColor.value = ContextCompat.getColor(
            Application.app,
            if (tx.amount[0] == '-') R.color.transactionAmountOutbound else R.color.transactionAmountInbound
        )
        _dateDividerVisibility.value = if (tx.isFirstInDate) View.VISIBLE else View.GONE
        val date = tx.date.toDate()
        _dateDivider.value = when {
            date.toDateTime().compareTo(DateTime.now()) == 0 -> "Today"
            date.minusDays(1).toDateTime().compareTo(DateTime.now()) == 0 -> "Yesterday"
            else -> date.toReadable()
        }
        _label.value = tx.description
        _image.value = tx.product.favicon[0].url.replace(" ", "")
        imagePlaceholder = R.drawable.transaction_placeholder
        return this
    }

    interface ActionListener {
        fun onTransactionPressed()
    }

}
