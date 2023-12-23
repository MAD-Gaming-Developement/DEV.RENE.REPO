package dev.game.sharkyslot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomeNavigation : AppCompatActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnPolicy:Button
    private lateinit var settingsButton:ImageView

    private var gameSetting: GameSetting? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_navigation)

        gameSetting = GameSetting(this)
        btnPlay =findViewById(R.id.play)
        btnPolicy=findViewById(R.id.policy)
        settingsButton = findViewById(R.id.mainSettings)

        val policyDialog = PolicyDialog(this)
        policyDialog.showPolicyDialog(this)

        btnPlay.setOnClickListener{
            val intent = Intent(this@HomeNavigation, SharkySlot::class.java)
            startActivity(intent)
        }

        btnPolicy.setOnClickListener {
            policyDialog.showPolicyReview()
        }

        settingsButton.setOnClickListener {
            gameSetting?.showSettingsDialog()
        }


    }

    override fun onPause() {
        super.onPause()
        gameSetting!!.stopSounds()
    }

    override fun onResume() {
        super.onResume()
        gameSetting!!.resumeSounds()
    }
}