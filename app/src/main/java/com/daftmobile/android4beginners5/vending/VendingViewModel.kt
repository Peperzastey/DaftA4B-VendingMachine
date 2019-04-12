package com.daftmobile.android4beginners5.vending

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.daftmobile.android4beginners5.SingleLiveEvent

class VendingViewModel: ViewModel() {
    private val vendingMachine = ChocoBarVendingMachine()
    private val chocoBarLiveData = SingleLiveEvent<String>()
    private val depositLiveData = MutableLiveData<String>()
    private val errorLiveData = SingleLiveEvent<String>()

    fun deposit(): LiveData<String> = depositLiveData
    fun chocoBarVended(): LiveData<String> = chocoBarLiveData
    fun vendingError(): LiveData<String> = errorLiveData

    init {
        refreshDeposit()
        // thus chocoBarLiveData's observer will be called right after app run
    }

    fun depositCoin() {
        vendingMachine.depositCoin()
        refreshDeposit()
    }

    fun vend(barName: String) {
        try {
            val bar = vendingMachine.vend(barName)
            chocoBarLiveData.value = "You vended ${bar.name}"
            refreshDeposit()
        } catch (e: InsufficientFundsException) {
            errorLiveData.value = "Nie masz kasy. Poczebujesz ${e.coinsNeeded}"
        } catch (e: OutOfStockException) {
            errorLiveData.value = "Skończył się ${e.barName}"
        } catch (e: ItemNotFoundException) {
            errorLiveData.value = "Nie znam ${e.itemName}"
        } catch (e: Exception) {
            errorLiveData.value = e.message
        }
    }
    //TODO inline?
    private fun refreshDeposit() {
        depositLiveData.value = "Coins: ${vendingMachine.getCurrentDeposit()}"
    }
}
