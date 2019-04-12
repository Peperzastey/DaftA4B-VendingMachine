package com.daftmobile.android4beginners5.vending

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.daftmobile.android4beginners5.R
import kotlinx.android.synthetic.main.activity_vending.*

private const val TAG = "VendingActivity"

class VendingActivity : AppCompatActivity() {

    private val viewModel: VendingViewModel by lazy { ViewModelProviders.of(this).get(VendingViewModel::class.java) }

    private var dialogRef: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vending)
        setupClickListeners()
        observeData()
    }

    private fun observeData() {
        viewModel.chocoBarVended().observe(this, Observer(this::showVendedDialog))
        viewModel.vendingError().observe(this, Observer(this::showVendingErrorDialog))
        viewModel.deposit().observe(this, Observer(this::updateDeposit))
        // Dialogs not shown until the first setValue() calls on the corresponding (Mutable)LiveData-s
    }

    private fun setupClickListeners() {
        depositButton.setOnClickListener {
            viewModel.depositCoin()
        }
        vendButton.setOnClickListener {
            viewModel.vend(barNameInput.text.toString())
        }
    }

    private fun showVendingErrorDialog(error: String?) {
        dialogRef =
            AlertDialog.Builder(this)
                .setTitle(R.string.nope)
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, null)
                //.setOnDismissListener { dialogRef = null }
                .show()
        //fixed: *window leaked* on screen rotation when not dismissed (closed)
    }

    private fun showVendedDialog(chocoBar: String?) {
        dialogRef =
            AlertDialog.Builder(this)
                .setTitle(R.string.yay)
                .setMessage(chocoBar)
                .setPositiveButton(android.R.string.ok, null)
                //.setOnDismissListener { dialogRef = null }
                .show()
        //fixed: *window leaked* on screen rotation when not dismissed (closed)
        //in onDestroy / onStop / onPause
        //Call dismiss() on the Dialog instance you created before exiting your Activity, e.g. in onPause() or onDestroy() (depending on the desired behaviour - see: Activity lifecycle)
        //NOTE: All Windows & Dialogs should be closed before leaving an Activity
    }

    private fun updateDeposit(deposit: String?) {
        depositView.text = deposit
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        dialogRef?.dismiss()
        dialogRef = null
        super.onDestroy()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        //dialogRef?.dismiss()
        //dialogRef = null
        super.onStop()
    }
}
