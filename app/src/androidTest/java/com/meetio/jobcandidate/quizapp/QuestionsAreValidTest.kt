package com.meetio.jobcandidate.quizapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.meetio.jobcandidate.quizapp.quiz.data.Question
import com.meetio.jobcandidate.quizapp.quiz.data.Questions

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Test that checks that the questions given in the assets are correct.
 */
@RunWith(AndroidJUnit4::class)
class QuestionsAreValidTest {

    private lateinit var questions: ArrayList<Question>

    @Before
    fun createDb() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val fileInString: String =
            appContext.assets.open("questions.json").bufferedReader().use { it.readText() }
        questions = Gson().fromJson(fileInString, Questions::class.java).questions
    }

    @Test
    fun validateQuestionIdsFromJson() {
        var errorMsg = ""
        val duplicates = hasDuplicates(questions) { q1, q2 ->
            val result = q1.questionId != q2.questionId
            if (!result) {
                errorMsg = "Duplicate questionId in json: $q1 - $q2"
            }
            result
        }

        assertFalse(errorMsg, duplicates)
    }

    @Test
    fun validateQuestionTextsFromJson() {
        var errorMsg = ""
        val duplicates = hasDuplicates(questions) { q1, q2 ->
            val result = !q1.questionText.equals(q2.questionText, true)
            if (!result) {
                errorMsg = "Duplicate questionText in json: $q1 - $q2"
            }
            result
        }

        assertFalse(errorMsg, duplicates)
    }

    private fun hasDuplicates(
        list: ArrayList<Question>,
        validate: (Question, Question) -> Boolean
    ): Boolean {
        for (i in list.indices) {
            for (j in i + 1 until list.size) {
                if (!validate(list[i], list[j])) {
                    return true
                }
            }
        }
        return false
    }
}