package com.dputera.cigc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.royrodriguez.transitionbutton.TransitionButton
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: NumberAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean(COUNTER_SET, false)){
            goToHome()
        }

        val listNumber = ArrayList<Int>()
        for (i in 1..20) {
            listNumber.add(i)
        }

        manager = GridLayoutManager(this, 4)
        adapter = NumberAdapter(listNumber, null)
        rv = findViewById(R.id.rv_number)
        rv.layoutManager = manager
        rv.adapter = adapter
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setComment(comments: String, position: Int) {
        val comment = findViewById<TextView>(R.id.txt_choice_comment)
        val btnOk = findViewById<TransitionButton>(R.id.btn_ok)
        comment.visibility = View.VISIBLE
        comment.text = comments

        val listNumber = ArrayList<Int>()
        for (i in 1..20) {
            listNumber.add(i)
        }

        rv.adapter = null
        rv.adapter = NumberAdapter(listNumber, position)

        btnOk.text = getString(R.string.continues)

        btnOk.setOnClickListener {
            btnOk.startAnimation()

            val handler = Handler()
            handler.postDelayed({
                val isSuccessful = true
                if (isSuccessful) {
                    btnOk.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND
                    ) {
                        var maxStick = position.plus(1)
                        if (TESTING) maxStick = 24 * 60
                        val timeForAddition: Long = A_DAY_IN_MILLIS / maxStick.toLong()
                        val startTime = Calendar.getInstance().timeInMillis
                        sharedPreferences.edit()
                            .putInt(MAX_STICKS, maxStick)
                            .putLong(START_TIME, startTime)
                            .putLong(TIME_PER_STICK, timeForAddition)
                            .putLong(LAST_ADDITION, startTime)
                            .putInt(CURRENT_STICK, 1)
                            .putBoolean(COUNTER_SET, true)
                            .apply()
                        goToHome()
                    }
                } else {
                    btnOk.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }
            }, 2000)
        }

    }
}