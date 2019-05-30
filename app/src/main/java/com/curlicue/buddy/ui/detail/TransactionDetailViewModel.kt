package com.curlicue.buddy.ui.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.curlicue.buddy.Application
import com.curlicue.buddy.BindableFieldTarget
import com.curlicue.buddy.R
import com.curlicue.buddy.Utils
import com.curlicue.buddy.Utils.toDate
import com.curlicue.buddy.Utils.toReadable
import com.curlicue.buddy.data.TransactionDAO
import com.squareup.picasso.Picasso
import java.util.*


class TransactionDetailViewModel : ViewModel(), TransactionsDetailContract.ViewModel{

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _label = MutableLiveData<String>()
    val label: LiveData<String>
        get() = _label

    private val _backArrowPressed = MutableLiveData<Boolean>()
    val backArrowPressed: LiveData<Boolean>
        get() = _backArrowPressed

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount

    private val _amountColor = MutableLiveData<Int>()
    val amountColor: LiveData<Int>
        get() = _amountColor

    private val _currency = MutableLiveData<String>()
    val currency: LiveData<String>
        get() = _currency

    private val _image = MutableLiveData<Drawable>()
    val image: LiveData<Drawable>
        get() = _image

    private val _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    private val _tag = MutableLiveData<String>()
    val tag: LiveData<String>
        get() = _tag

    private val _tagBackground = MutableLiveData<Drawable>()
    val tagBackground: LiveData<Drawable>
        get() = _tagBackground

    fun start(tx: TransactionDAO.Transaction): TransactionDetailViewModel {
        _name.value = tx.product.title
        _currency.value = Currency.getInstance(tx.currency).symbol
        _amount.value = if (tx.amount[0] == '-') tx.amount.replace("-", "") else "+ ${tx.amount}"
        _amountColor.value = ContextCompat.getColor(
            Application.app,
            if (tx.amount[0] == '-') R.color.transactionAmountOutbound else R.color.transactionAmountInbound
        )
        _tag.value = tx.categoryMeta.categoryName
        _tagBackground.value= Utils.makeOblong(Color.parseColor(tx.categoryMeta.colour))
        _date.value = tx.date.toDate().toReadable()
        _label.value = tx.description
        Picasso.get()
            .load(tx.product.favicon[0].url.replace(" ", ""))
            .placeholder(R.drawable.transaction_placeholder)
            .into(BindableFieldTarget(_image, Application.app.resources))
        return this
    }

    override fun onBackPressed() {
        _backArrowPressed.value = true
    }
}
