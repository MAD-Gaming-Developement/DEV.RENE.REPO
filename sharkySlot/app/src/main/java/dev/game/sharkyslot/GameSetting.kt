package dev.game.sharkyslot

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.view.Gravity
import android.view.View
import android.widget.ImageView

class GameSetting(private val context: Context) {
    companion object {
        lateinit var pref: SharedPreferences
        const val PREFS_FIRST_RUN = "FirstRun"
        private const val PREFS_MUSIC = "music"
        private const val PREFS_SOUND = "sound"

        private var playMusic = 0
        private var playSound = 0
        @SuppressLint("StaticFieldLeak")
        private lateinit var musicOff: ImageView
        @SuppressLint("StaticFieldLeak")
        lateinit var musicOn: ImageView
        @SuppressLint("StaticFieldLeak")
        lateinit var btnOn: ImageView
        @SuppressLint("StaticFieldLeak")
        private lateinit var btnOff: ImageView
        lateinit var backgroundSound: MediaPlayer
    }

    init {
        pref = context.getSharedPreferences(PREFS_FIRST_RUN, Context.MODE_PRIVATE)
        val firstRun = pref.getBoolean(PREFS_FIRST_RUN, true)

        if (firstRun) {
            playMusic = 1
            playSound = 1
            val editor = pref.edit()
            editor.putBoolean(PREFS_FIRST_RUN, false)
            editor.apply()
        } else {
            playMusic = pref.getInt(PREFS_MUSIC, 1)
            playSound = pref.getInt(PREFS_SOUND, 1)
        }

        backgroundSound = MediaPlayer.create(context, R.raw.bg_music)
        backgroundSound.isLooping = true


        if (playMusic == 1) {
            checkMusic()
        }
    }

    fun showSettingsDialog() {
        val dialog = Dialog(context, R.style.WinDialog)
        dialog.window?.setContentView(R.layout.settings)

        dialog.window?.setGravity(Gravity.CENTER_HORIZONTAL)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        val close = dialog.findViewById<ImageView>(R.id.close)
        close.setOnClickListener { dialog.dismiss() }

        musicOn = dialog.findViewById(R.id.music_on)
        musicOn.setOnClickListener {
            playMusic = 0
            checkMusic()
            musicOn.visibility = View.INVISIBLE
            musicOff.visibility = View.VISIBLE
            val editor = pref.edit()
            editor.putInt(PREFS_MUSIC, playMusic)
            editor.apply()
        }

        musicOff = dialog.findViewById(R.id.music_off)
        musicOff.setOnClickListener {
            playMusic = 1
            backgroundSound.start()
            musicOn.visibility = View.VISIBLE
            musicOff.visibility = View.INVISIBLE
            val editor = pref.edit()
            editor.putInt(PREFS_MUSIC, playMusic)
            editor.apply()
        }

        btnOn = dialog.findViewById(R.id.sounds_on)
        btnOn.setOnClickListener {
            playSound = 0
            btnOn.visibility = View.INVISIBLE
            btnOff.visibility = View.VISIBLE
            val editor = pref.edit()
            editor.putInt(PREFS_SOUND, playSound)
            editor.apply()
        }

        btnOff = dialog.findViewById(R.id.sounds_off)
        btnOff.setOnClickListener {
            playSound = 1
            dialog.show()
            btnOn.visibility = View.VISIBLE
            btnOff.visibility = View.INVISIBLE
            val editor = pref.edit()
            editor.putInt(PREFS_SOUND, playSound)
            editor.apply()
        }

        checkMusicDraw()
        checkSoundDraw()

        dialog.show()
    }

    fun resumeSounds() {
        if (playMusic == 1) {
            backgroundSound.start()
        }
    }

    fun stopSounds() {
        if (backgroundSound.isPlaying) {
            backgroundSound.pause()
        }

        // Similarly for mp and win
    }

    private fun checkMusic() {
        if (playMusic == 1) {
            backgroundSound.start()
        } else {
            backgroundSound.pause()
        }
    }

    private fun checkMusicDraw() {
        if (playMusic == 1) {
            musicOn.visibility = View.VISIBLE
            musicOff.visibility = View.INVISIBLE
        } else {
            musicOn.visibility = View.INVISIBLE
            musicOff.visibility = View.VISIBLE
        }
    }

    private fun checkSoundDraw() {
        if (playSound == 1) {
            btnOn.visibility = View.VISIBLE
            btnOff.visibility = View.INVISIBLE
        } else {
            btnOn.visibility = View.INVISIBLE
            btnOff.visibility = View.VISIBLE
        }
    }

    fun playSpinSound() {
        if (playSound == 1) {
            val mp = MediaPlayer.create(context, R.raw.spin)
            mp.start()
        }
    }

    fun stopSpinSound(){
        if (playSound == 1) {
            val st = MediaPlayer.create(context, R.raw.stopsound)
            st.start()
        }
    }

    fun playWinSound() {
        if (playSound == 1) {
            val win = MediaPlayer.create(context, R.raw.win)
            win.start()
        }
    }

}
