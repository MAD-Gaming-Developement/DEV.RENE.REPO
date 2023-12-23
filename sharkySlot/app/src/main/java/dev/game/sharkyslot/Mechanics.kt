package dev.game.sharkyslot


import java.util.*

class Mechanics {
    private var myCoins = 0
    private var bet = 0
    private var jackpot = 0
    private var prize = 0
    private var hasWon = false

    private val slots = intArrayOf(1, 1, 1)

    private fun getRandomInt(): Int {
        val random = Random()
        return random.nextInt(7) + 1
    }

    fun getPosition(i: Int): Int {
        return slots[i] + 5
    }

    fun getSpinResults() {
        prize = 0
        val previousCoins = myCoins

        for (i in slots.indices) {
            slots[i] = getRandomInt()
        }

        var countSevens = 0
        var matchedValue = -1

        for (a in slots) {
            if (a == 7) {
                countSevens++
            }
        }

        if (countSevens == 3) {
            hasWon = true
            prize = jackpot
            jackpot = 0
            myCoins += prize
        } else {
            var matchedPairs = 0

            for (i in 0 until slots.size - 1) {
                for (j in i + 1 until slots.size) {
                    if (slots[i] == slots[j]) {
                        matchedPairs++
                        matchedValue = slots[i]
                    }
                }
            }

            if (matchedPairs == 1) {
                when (matchedValue) {
                    1 -> {
                        hasWon = true
                        prize = bet * 2
                        myCoins += prize
                    }
                    2 -> {
                        hasWon = true
                        prize = bet * 3 + 5
                        myCoins += prize
                    }
                    3 -> {
                        hasWon = true
                        prize = bet * 4 + 10
                        myCoins += prize
                    }
                    4 -> {
                        hasWon = true
                        prize = bet * 5 + 15
                        myCoins += prize
                    }
                    5 -> {
                        hasWon = true
                        prize = bet * 6 +20
                    }

                    6 -> {
                        hasWon = true
                        prize = bet * 7 + 25
                        myCoins += prize
                    }
                    7 -> {
                        hasWon = true
                        prize = bet * 8 + 30
                        myCoins += prize
                    }
                }
            }
        }


        if (!hasWon) {
            myCoins -= bet
            jackpot += bet
        }
    }


    fun setMyCoins(myCoins: Int) {
        this.myCoins = myCoins
    }

    fun setBet(bet: Int) {
        this.bet = bet
    }

    fun setJackpot(jackpot: Int) {
        this.jackpot = jackpot
    }

    fun betUp() {
        if (bet < 100) {
            bet += 5
            myCoins -= 5
        }
    }

    fun betDown() {
        if (bet > 5) {
            bet -= 5
            myCoins += 5
        }
    }

    fun setHasWon(hasWon: Boolean) {
        this.hasWon = hasWon
    }


    fun getMyCoins(): String {
        return myCoins.toString()
    }


    fun getBet(): String {
        return bet.toString()
    }

    fun getJackpot(): String {
        return jackpot.toString()
    }

    fun getHasWon(): Boolean {
        return hasWon
    }

    fun getPrize(): String {
        return prize.toString()
    }
}
