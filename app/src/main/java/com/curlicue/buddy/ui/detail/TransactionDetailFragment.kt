package com.curlicue.buddy.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.curlicue.buddy.Utils
import com.curlicue.buddy.data.TransactionDAO
import com.curlicue.buddy.databinding.FragmentTransactionDetailBinding


class TransactionDetailFragment : Fragment() {

    companion object {
        enum class Args(val key: String) { Transaction("tx") }

        fun newInstance(transaction: TransactionDAO.Transaction): TransactionDetailFragment {
            val fragment = TransactionDetailFragment()
            val args = Bundle()
            args.putString(Args.Transaction.key, Utils.jsonToString(transaction))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModel = ViewModelProviders.of(this).get(TransactionDetailViewModel::class.java).start(
                Utils.stringToJson<TransactionDAO.Transaction>(arguments!!.getString(Args.Transaction.key))!!
        )
        val binding = FragmentTransactionDetailBinding.inflate(inflater, container, false).apply {
            this.lifecycleOwner = this@TransactionDetailFragment.viewLifecycleOwner
            this.viewModel = viewModel
        }

        viewModel.backArrowPressed.observe(this, Observer<Boolean> {
            if (it) {
                activity!!.onBackPressed()
            }
        })

        return binding.root
    }

}
