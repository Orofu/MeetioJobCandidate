package com.meetio.jobcandidate.quizapp.quiz

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.meetio.jobcandidate.quizapp.quiz.data.Question
import com.meetio.jobcandidate.quizapp.quiz.data.Questions

private const val NUMBER_OF_QUESTIONS = 10

class QuizViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val fileInString: String =
            application.assets.open("questions.json").bufferedReader().use { it.readText() }
        val questionsGson = Gson().fromJson(fileInString, Questions::class.java)

        val questions = questionsGson.questions.also { it.shuffle() }.take(NUMBER_OF_QUESTIONS)

        return QuizViewModel(application, questions as ArrayList<Question>) as T
    }
}