package com.meetio.jobcandidate.quizapp.quiz.data

data class Question(
    val questionId: Int,
    val questionText: String,
    val questionType: String,
    val questionImage: String,
    val answers: ArrayList<Answer>
)