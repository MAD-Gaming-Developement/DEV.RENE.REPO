package dev.game.sharkyslot


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class SharkySlot : AppCompatActivity() {


    private val key1 = 150
    private val key2 = 200
    private val key3 = 250
    private var position1 = 5
    private var position2 = 5
    private var position3 = 5
    private val slot = intArrayOf(1, 2, 3, 4, 5, 6, 7)

    private lateinit var recycler1: RecyclerView
    private lateinit var recycler2: RecyclerView
    private lateinit var recycler3: RecyclerView
    private lateinit var layoutManager1: LayoutScrollSetting
    private lateinit var layoutManager2: LayoutScrollSetting
    private lateinit var layoutManager3: LayoutScrollSetting

    private lateinit var energyBallPrice: TextView
    private lateinit var myPower: TextView
    private lateinit var bet: TextView

    private var myCoinsVal = 0
    private var betVal = 0
    private var jackpotVal = 0

    private var firstRun = false

    private lateinit var gameLogic: Mechanics
    private lateinit var pref: SharedPreferences
    private lateinit var gameSetting: GameSetting

    private lateinit var minusButton: ImageButton
    private lateinit var plusButton: ImageButton
    private lateinit var adapter: SpinnerAdapter
    private lateinit var settingsButton: ImageView
    private lateinit var spinButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.main3)


        gameSetting = GameSetting(this)
        gameSetting.resumeSounds()

        pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        firstRun = pref.getBoolean("firstRun", true)


        gameLogic = Mechanics()
        settingsButton = findViewById(R.id.settings)
        spinButton = findViewById(R.id.spinButton)
        plusButton = findViewById(R.id.plusButton)
        minusButton = findViewById(R.id.minusButton)
        energyBallPrice = findViewById(R.id.energyBall)
        myPower = findViewById(R.id.energy)
        bet = findViewById(R.id.bet)
        adapter = SpinnerAdapter(this, slot)


        recycler1 = findViewById(R.id.spinner1)
        recycler2 = findViewById(R.id.spinner2)
        recycler3= findViewById(R.id.spinner3)

        fixSize()
        layoutLinear()
        setText()
        updateText()

        recycler1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    recycler1.scrollToPosition(gameLogic.getPosition(0))
                    layoutManager1.setScrollEnabled(false)
                }
            }
        })

        recycler2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    recycler2.scrollToPosition(gameLogic.getPosition(1))
                    layoutManager2.setScrollEnabled(false)
                }
            }
        })

        recycler3.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    recycler3.scrollToPosition(gameLogic.getPosition(2))
                    layoutManager3.setScrollEnabled(false)
                    updateText()
                    handleWin()
                }
            }
        })

        spinButton.setOnClickListener {
            gameSetting.playSpinSound()
            layoutManager1.setScrollEnabled(true)
            layoutManager2.setScrollEnabled(true)
            layoutManager3.setScrollEnabled(true)
            gameLogic.getSpinResults()
            position1 = gameLogic.getPosition(0) + key1
            position2 = gameLogic.getPosition(1) + key2
            position3 = gameLogic.getPosition(2) + key3
            recycler1.smoothScrollToPosition(position1)
            recycler2.smoothScrollToPosition(position2)
            recycler3.smoothScrollToPosition(position3)

        }

        plusButton.setOnClickListener {
            gameLogic.betUp()
            updateText()
        }

        minusButton.setOnClickListener {
            gameLogic.betDown()
            updateText()
        }

        settingsButton.setOnClickListener { gameSetting.showSettingsDialog() }
    }


    private fun layoutLinear(){
        layoutManager1 = LayoutScrollSetting(this)
        layoutManager1.setScrollEnabled(false)
        recycler1.layoutManager = layoutManager1
        layoutManager2 = LayoutScrollSetting(this)
        layoutManager2.setScrollEnabled(false)
        recycler2.layoutManager = layoutManager2
        layoutManager3 = LayoutScrollSetting(this)
        layoutManager3.setScrollEnabled(false)
        recycler3.layoutManager = layoutManager3

        recycler1.adapter = adapter
        recycler2.adapter = adapter
        recycler3.adapter = adapter
        recycler1.scrollToPosition(position1)
        recycler2.scrollToPosition(position2)
        recycler3.scrollToPosition(position3)
    }

    private fun setText() {
        if (firstRun) {
            gameLogic.setMyCoins(1000)
            gameLogic.setBet(5)
            gameLogic.setJackpot(100000)

            val editor = pref.edit()
            editor.putBoolean("firstRun", false)
            editor.apply()
        } else {
            val coins = pref.getString("coins", "") ?: ""
            val bet = pref.getString("bet", "") ?: ""
            val jackpot = pref.getString("jackpot", "") ?: ""
            Log.d("COINS", coins)
            myCoinsVal = coins.toIntOrNull() ?: 0
            betVal = bet.toIntOrNull() ?: 0
            jackpotVal = jackpot.toIntOrNull() ?: 0
            gameLogic.setMyCoins(myCoinsVal)
            gameLogic.setBet(betVal)
            gameLogic.setJackpot(jackpotVal)
        }
    }

    private fun fixSize(){
        recycler1.setHasFixedSize(true)
        recycler2.setHasFixedSize(true)
        recycler3.setHasFixedSize(true)

    }

    private fun updateText() {
        energyBallPrice.text = gameLogic.getJackpot()
        myPower.text = gameLogic.getMyCoins()
        bet.text = gameLogic.getBet()

        val editor = pref.edit()
        editor.putString("coins", gameLogic.getMyCoins())
        editor.putString("bet", gameLogic.getBet())
        editor.putString("jackpot", gameLogic.getJackpot())
        editor.apply()
    }

    private fun handleWin() {
        if (gameLogic.getHasWon()) {
            gameSetting.playWinSound()
            val inflater = LayoutInflater.from(this@SharkySlot)
            val layout = inflater.inflate(R.layout.win_splash, findViewById(R.id.win_splash))
            val winCoins = layout.findViewById<TextView>(R.id.win_coins)
            winCoins.text = gameLogic.getPrize()
            val toast = Toast(this@SharkySlot)
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.view = layout
            toast.show()
            gameLogic.setHasWon(false)
        }
    }

        override fun onPause() {
            super.onPause()
            gameSetting.stopSounds()
        }

        override fun onResume() {
            super.onResume()
            gameSetting.resumeSounds()
        }

        companion object {
            const val PREFS_NAME = "FirstRun"
        }
}
