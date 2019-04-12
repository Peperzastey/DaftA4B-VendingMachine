package com.daftmobile.android4beginners5.vending


class ChocoBarVendingMachine {

    //TODO check: Map instead of MutableMap (changing only the amount in machine, not products)
    private val itemsCount = mutableMapOf(
            Bar("Princess Polo", 4) to 2,
            Bar("Venus", 3) to 3,
            Bar("Silky Way", 2) to 2
    )
    //TODO check: is String mutable or not? (Can you change a val String?)
    private var deposit = 0
    //TODO kotlin: can you change a value (by reference) of r.g. getItemsMap() ?
    fun getCurrentDeposit() = deposit

    fun depositCoin() {
        //deposit++
        ++deposit
    }

    fun vend(itemName: String): Bar {
        val item = itemsCount.keys.find { it.name == itemName } ?: throw ItemNotFoundException(itemName)
        if (itemsCount.getValue(item) <= 0) throw OutOfStockException(item.name)
        if (item.price > deposit) throw InsufficientFundsException(item.price)
        itemsCount[item] = itemsCount.getValue(item) - 1
        deposit -= item.price
        //TODO question: copy so you cannot change the returned reference (?) (and wreck the MutableMap?)
        return item.copy()
    }

}