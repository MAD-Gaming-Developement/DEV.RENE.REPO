package dev.game.sharkyslot

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.app.ActivityCompat.finishAffinity

class PolicyDialog(private val context: Context) {

    private val prefPolicy= "PolicyAccepted"

    fun showPolicyDialog(activity: Activity) { // Accept an Activity reference
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.policy2) // Replace with your actual layout XML

        val acceptButton = dialog.findViewById<Button>(R.id.accept)
        val rejectButton = dialog.findViewById<Button>(R.id.reject)
        val policyWeb = dialog.findViewById<WebView>(R.id.policy)

        val sharedPreferences =
            context.getSharedPreferences("PolicyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        policyWeb.loadUrl("file:///android_res/raw/policy.html")

        acceptButton.setOnClickListener {
            editor.putBoolean(prefPolicy, true)
            editor.apply()
            dialog.dismiss()
        }

        rejectButton.setOnClickListener {
            finishAffinity(activity) // Use the provided Activity reference
            dialog.dismiss()
        }

        val policyAccepted = sharedPreferences.getBoolean(prefPolicy, false)
        if (!policyAccepted) {
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    fun showPolicyReview() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_policy_review)

        val policyWeb = dialog.findViewById<WebView>(R.id.policy)
        val close=dialog.findViewById<ImageView>(R.id.closePolicy)

        val sharedPreferences =
            context.getSharedPreferences("PolicyPreferences", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        policyWeb.loadUrl("file:///android_res/raw/policy.html")

        close.setOnClickListener{
            dialog.dismiss()
        }


        dialog.show()
    }

}
