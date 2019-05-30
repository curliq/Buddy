package com.curlicue.buddy.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.curlicue.buddy.R
import com.curlicue.buddy.data.TransactionDAO
import com.curlicue.buddy.databinding.FragmentTransactionsBinding
import com.curlicue.buddy.ui.MainActivity
import com.curlicue.buddy.ui.detail.TransactionDetailFragment


class TransactionsListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel = ViewModelProviders.of(this).get(TransactionsListViewModel::class.java).start()
        val txRecyclerAdapter = TransactionsRecyclerAdapter(viewModel, this)

        val binding = FragmentTransactionsBinding.inflate(inflater, container, false).apply {
            this.viewModel = viewModel
            this.lifecycleOwner = this@TransactionsListFragment
            this.executePendingBindings()
            this.fragmentTransactionsTransactionsRv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL,
                    false
                )
                adapter = txRecyclerAdapter
            }
            val dividerDecorator = DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL)

            dividerDecorator.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider_horizontal)!!)
            this.fragmentTransactionsTransactionsRv.addItemDecoration(dividerDecorator)
        }

        viewModel.transactionPressed.observe(this, Observer<TransactionDAO.Transaction> {
            it?.let {
                viewModel.onTransactionPressed(null)
                (activity as MainActivity).inflateFragment(TransactionDetailFragment.newInstance(it), true, true)
            }
        })

        viewModel.transactions.observe(this, Observer<List<TransactionDAO.Transaction>> { data ->
            data?.let {
                txRecyclerAdapter.updateData(it)
            }
        })

        binding.fragmentTransactionsTransactionsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.onListScrolled(binding.fragmentTransactionsTransactionsRv.computeVerticalScrollOffset())
            }
        })

        return binding.root
    }

}
