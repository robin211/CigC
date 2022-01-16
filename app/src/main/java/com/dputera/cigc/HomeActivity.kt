package com.dputera.cigc

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.transition.*
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.royrodriguez.transitionbutton.TransitionButton
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class HomeActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private val slideOutTop: Transition = Slide(Gravity.TOP)
    private val slideInEnd: Transition = Slide(Gravity.END)
    private val slideInStart: Transition = Slide(Gravity.START)
    private lateinit var container: ViewGroup
    private lateinit var cig1: RelativeLayout
    private lateinit var cig0: RelativeLayout
    private lateinit var lighter: ImageView
    private lateinit var fire: ImageView
    private lateinit var lighted: ImageView
    private lateinit var smooth: TextView
    private lateinit var lightup: ImageView
    private lateinit var availStick: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        container = findViewById(R.id.layout_container)
        cig1 = findViewById(R.id.cig1)
        cig0 = findViewById(R.id.cig0)
        lighter = findViewById(R.id.lighter)
        fire = findViewById(R.id.fire)
        lighted = findViewById(R.id.lighted)
        smooth = findViewById(R.id.smooth)
        lightup = findViewById(R.id.lightup)
        availStick = findViewById(R.id.available_sticks)

        sharedPreferences = this.getSharedPreferences(SP_NAME, MODE_PRIVATE)

        calculateAvailableStick()

        findViewById<ImageView>(R.id.resets).setOnClickListener {
            showDialog()
        }

        lightup.setOnClickListener {
            if (sharedPreferences.getInt(CURRENT_STICK, 0) > 0){
                lightup.visibility = View.GONE
                startSmokingAnimationSequence()
                smokeOneSticks()
            }else {
                showDialogTwo()
            }
        }

    }

    private fun smokeOneSticks() {
        val currentStick = sharedPreferences.getInt(CURRENT_STICK, 0)
        val afterSmoked = currentStick.minus(1)
        sharedPreferences.edit().putInt(CURRENT_STICK, afterSmoked).apply()
        calculateAvailableStick()
    }

    @SuppressLint("SetTextI18n")
    private fun calculateAvailableStick() {
        val currentStick = sharedPreferences.getInt(CURRENT_STICK, 0)
        val timeForOne = sharedPreferences.getLong(TIME_PER_STICK, 0L)
        val lastAddition = sharedPreferences.getLong(LAST_ADDITION, 0)
        val currentTime = Calendar.getInstance().timeInMillis
        val timePassed = currentTime.minus(lastAddition)
        val additionalStick = timePassed/timeForOne

        if (additionalStick > 0){
            val additionalTime = timeForOne * additionalStick
            val lastAdditionime = lastAddition.plus(additionalTime)
            sharedPreferences.edit().putLong(LAST_ADDITION, lastAdditionime).apply()
        }

        val totalStick = currentStick.plus(additionalStick)

        availStick.text = "$totalStick x "
        sharedPreferences.edit().putInt(CURRENT_STICK, totalStick.toInt()).apply()
    }

    private fun startSmokingAnimationSequence() {
        slideOutTop.duration = 500
        slideOutTop.interpolator = LinearOutSlowInInterpolator()

        slideInEnd.duration = 200
        slideInEnd.interpolator = FastOutSlowInInterpolator()

        slideInStart.duration = 200
        slideInStart.interpolator = FastOutSlowInInterpolator()

        val slowFade: TransitionSet = TransitionSet()
            .addTransition(Fade())
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(500)

        TransitionManager.beginDelayedTransition(layout_container, slideOutTop)
        cig1.visibility = View.GONE

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container, slideInEnd)
            cig0.visibility = View.VISIBLE
        }, 500)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container, slideInStart)
            lighter.visibility = View.VISIBLE
        }, 700)

        Handler().postDelayed({
            fire.visibility = View.VISIBLE
        }, 1100)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container)
            lighted.visibility = View.VISIBLE
        }, 1200)

        Handler().postDelayed({
            fire.visibility = View.GONE
        }, 1800)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container, slowFade)
            cig0.visibility = View.GONE
            lighted.visibility = View.GONE
            lighter.visibility = View.GONE
        }, 1950)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container, slowFade)
            smooth.visibility = View.VISIBLE
        }, 2450)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container, slowFade)
            smooth.visibility = View.GONE
        }, 3450)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container, slideOutTop)
            cig1.visibility = View.VISIBLE
        }, 3950)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(layout_container, slowFade)
            lightup.visibility = View.VISIBLE
        }, 4250)
    }

    override fun onRestart() {
        super.onRestart()
        calculateAvailableStick()
    }

    override fun onResume() {
        super.onResume()
        calculateAvailableStick()
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        val yesBtn = dialog.findViewById(R.id.btn_ok) as TransitionButton
        val noBtn = dialog.findViewById(R.id.btn_cancel) as TransitionButton
        yesBtn.setOnClickListener {
            dialog.dismiss()
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showDialogTwo() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_two_layout)
        val yesBtn = dialog.findViewById(R.id.btn_ok) as TransitionButton
        val noBtn = dialog.findViewById(R.id.btn_cancel) as TransitionButton
        yesBtn.setOnClickListener {
            dialog.dismiss()
            lightup.visibility = View.GONE
            startSmokingAnimationSequence()
            smokeOneSticks()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}