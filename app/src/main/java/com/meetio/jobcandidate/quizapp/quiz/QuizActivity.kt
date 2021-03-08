package com.meetio.jobcandidate.quizapp.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meetio.jobcandidate.quizapp.R
import com.meetio.jobcandidate.quizapp.addFragment
import com.meetio.jobcandidate.quizapp.hideSystemUi
import com.meetio.jobcandidate.quizapp.quiz.fragments.QuizFragment

class QuizActivity : AppCompatActivity(R.layout.activity_quiz) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi()

        // TODO: Use somewhere..
        //val name = activity.intent.getStringExtra(BUNDLE_USER_NAME)

        if (savedInstanceState == null) {
            addFragment(QuizFragment(), R.id.activity_quiz_container)
        }
    }
}