package com.meetio.jobcandidate.quizapp.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.meetio.jobcandidate.quizapp.BUNDLE_USER_NAME
import com.meetio.jobcandidate.quizapp.R
import com.meetio.jobcandidate.quizapp.quiz.QuizActivity
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcome_start_btn.setOnClickListener {
            val name = welcome_input_text.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(
                    activity, R.string.welcome_no_name_entered,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(activity, QuizActivity::class.java)
                intent.putExtra(BUNDLE_USER_NAME, name)
                startActivity(intent)
            }
        }
    }
}