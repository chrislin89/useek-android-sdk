package com.useek.example_kotlin.kotlin_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.widget.LinearLayout
import com.useek.example_kotlin.ExampleSettingsManager
import com.useek.example_kotlin.R
import com.useek.library_kt_beta.USeekManager
import com.useek.library_kt_beta.USeekPlayerListener
import com.useek.library_kt_beta.USeekPlayerView
import kotlinx.android.synthetic.main.activity_custom_view_programmatically.*

class CustomViewProgrammaticallyActivity : AppCompatActivity(), USeekPlayerListener {

    private lateinit var useekPlayerView: USeekPlayerView


    private val settingsManager = ExampleSettingsManager.sharedInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view_programmatically)

        addUSeekPlayerView()
        buttonScore.setOnClickListener { onPressedGetScore() }
    }

    private fun addUSeekPlayerView() {
        useekPlayerView = USeekPlayerView(this)
        useekPlayerView.loadingText = ExampleSettingsManager.sharedInstance.loadingText
        useekContainer.addView(
                useekPlayerView.getView(),
                LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        )
        useekPlayerView.playerListener = this
        USeekManager.sharedInstance.publisherId = settingsManager.publisherId
        if (settingsManager.gameId != null && settingsManager.userId != null)
            useekPlayerView.loadVideo(settingsManager.gameId!!, settingsManager.userId!!)
    }

    private fun onPressedGetScore() {
        if (settingsManager.gameId == null) return
        txtScore.text = "Loading score..."
        buttonScore.isEnabled = false
        USeekManager.sharedInstance.requestPoints(
                settingsManager.gameId!!,
                settingsManager.userId
        )  { lastPlayPoints, totalPlayPoints, error ->

            if (error == null) {
                txtScore.text = String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints)
            } else {
                txtScore.text = error.toString()
            }
            buttonScore.isEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        useekPlayerView.destroy()
    }

    /** USeekPlayerView listener */

    override fun useekPlayerDidFailWithError(useekPlayerView: USeekPlayerView, error: WebResourceError?) {
        print("useekPlayerDidFailWithError video")
    }

    override fun useekPlayerDidStartLoad(useekPlayerView: USeekPlayerView) {
        print("useekPlayerDidStartLoad video")
    }

    override fun useekPlayerDidFinishLoad(useekPlayerView: USeekPlayerView) {
        print("useekPlayerDidFinishLoad video")
    }
}
