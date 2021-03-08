package com.meetio.jobcandidate.quizapp.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meetio.jobcandidate.quizapp.R
import com.meetio.jobcandidate.quizapp.addFragment
import com.meetio.jobcandidate.quizapp.hideSystemUi

class WelcomeActivity : AppCompatActivity(R.layout.activity_welcome) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi()

        if (savedInstanceState == null) {
            addFragment(WelcomeFragment(), R.id.activity_welcome_container)
        }
    }
}