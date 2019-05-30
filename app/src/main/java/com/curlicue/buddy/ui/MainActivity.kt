package com.curlicue.buddy.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.curlicue.buddy.R
import com.curlicue.buddy.Utils
import com.curlicue.buddy.ui.list.TransactionsListFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import androidx.transition.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENTS_CONTAINER_ID = R.id.activityMain_container_fl
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inflateFragment(TransactionsListFragment(), false)
    }

    fun inflateFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true,
        animate: Boolean = false
    ) {

        if (isFinishing || supportFragmentManager.isDestroyed)
            return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (animate)
            fragmentTransaction.setCustomAnimations(
                R.animator.fade_in_fast,
                R.animator.fade_out_fast,
                R.animator.fade_in_fast,
                R.animator.fade_out_fast
            )

        fragmentTransaction.replace(FRAGMENTS_CONTAINER_ID, fragment)

        if (addToBackStack)
            fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

}
