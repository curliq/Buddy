package com.curlicue.buddy.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.curlicue.buddy.R
import com.curlicue.buddy.Utils.loadImageUrl
import com.curlicue.buddy.data.TransactionDAO
import com.curlicue.buddy.databinding.ItemTransactionBinding


class TransactionsRecyclerAdapter(
    val viewModel: TransactionsListViewModel,
    val lifeCycleOwner: LifecycleOwner
) : RecyclerView.Adapter<TransactionsRecyclerAdapter.ViewHolder>() {

    private lateinit var txList: List<TransactionDAO.Transaction>
    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ItemTransactionBinding = DataBindingUtil.inflate(
            layoutInflater!!,
            R.layout.item_transaction,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(txList[position])
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }

    override fun getItemCount(): Int {
        return if (::txList.isInitialized) txList.size else 0
    }

    fun updateData(txList: List<TransactionDAO.Transaction>) {
        this.txList = txList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tx: TransactionDAO.Transaction) {
            binding.viewModel = TransactionItemViewModel(tx).start()
            binding.actionListener = object : TransactionItemViewModel.ActionListener {
                override fun onTransactionPressed() {
                    viewModel.onTransactionPressed(tx)
                }
            }
            binding.viewModel!!.image.observe(lifeCycleOwner, Observer<String> {
                binding.itemTransactionImageIv.loadImageUrl(it, binding.viewModel!!.imagePlaceholder)
            })
        }

        fun unBind() {
            binding.unbind()
        }

    }

}
