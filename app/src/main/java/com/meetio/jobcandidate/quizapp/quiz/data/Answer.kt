package com.meetio.jobcandidate.quizapp.quiz.data

import android.content.Context
import com.meetio.jobcandidate.quizapp.R

const val INVALID_ID = -1

data class Answer(
    val answerId: Int,
    val answer: String,
    val correct: Boolean
) {
    companion object {
        fun createTimedOutAnswer(context: Context): Answer {
            return Answer(INVALID_ID, context.getString(R.string.summary_result_is_time_out), false)
        }
    }
}