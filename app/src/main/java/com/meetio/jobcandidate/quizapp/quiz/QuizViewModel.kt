package com.meetio.jobcandidate.quizapp.quiz

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.meetio.jobcandidate.quizapp.SingleLiveEvent
import com.meetio.jobcandidate.quizapp.quiz.data.Answer
import com.meetio.jobcandidate.quizapp.quiz.data.Question
import kotlin.math.roundToInt

private const val DEFAULT_PROGRESS_MAX_TIME: Long = 15 * 1000 // Milliseconds
private const val PROGRESS_TIME_EXTRA: Long = 10 * 1000 // Milliseconds
private const val DEFAULT_PROGRESS_INTERVAL: Long = 1000 // Milliseconds

class QuizViewModel(
    application: Application,
    val questions: ArrayList<Question>
) : AndroidViewModel(application) {
    enum class LifeLine {
        FIFTY_FIFTY,
        ADD_TIME
    }

    private val lifeLines: HashMap<LifeLine, Boolean> = hashMapOf(
        LifeLine.FIFTY_FIFTY to true,
        LifeLine.ADD_TIME to true
    )
    private val result: ArrayList<Pair<Question, Answer>> = arrayListOf()

    private val newQuestionMutable: SingleLiveEvent<Unit> = SingleLiveEvent()
    val newQuestion: LiveData<Unit> = newQuestionMutable

    private val quizCompleteMutable: SingleLiveEvent<Unit> = SingleLiveEvent()
    val quizComplete: LiveData<Unit> = quizCompleteMutable

    var currentProgress = 1
        private set

    var countDownCallback: ((
        currentProgress: Int, timeLeft: Int, maxTime: Int,
        timeRunningOut: Boolean, finished: Boolean
    ) -> Unit)? = null

    private var countDownTimer: Timer = Timer(DEFAULT_PROGRESS_MAX_TIME, DEFAULT_PROGRESS_INTERVAL)

    fun getQuestion(): Question {
        val questionIndex = currentProgress - 1
        return questions[questionIndex]
    }

    fun giveAnswer(answer: Answer) {
        val question = getQuestion()
        result.add(Pair(question, answer))

        currentProgress++
        countDownTimer.reset()

        if (result.size < questions.size) {
            newQuestionMutable.value = Unit
        } else {
            quizCompleteMutable.value = Unit
        }
    }

    fun getAnswers(): ArrayList<Answer> {
        return getQuestion().answers.also { it.shuffle() }
    }

    fun getResult(): List<Pair<Question, Answer>> {
        return result
    }

    fun startCountDown(
        callback: (
            currentProgress: Int, timeLeft: Int, maxTime: Int,
            timeRunningOut: Boolean, finished: Boolean
        ) -> Unit
    ) {
        countDownCallback = callback
        countDownTimer.start()
    }

    fun useLifeLine(lifeLine: LifeLine, use: () -> Unit) {
        if (hasLifeLine(lifeLine)) {
            lifeLines[lifeLine] = false
            use.invoke()

            if (lifeLine == LifeLine.ADD_TIME) {
                countDownTimer.addTime()
            }
        }
    }

    fun hasLifeLine(lifeLine: LifeLine): Boolean {
        return lifeLines[lifeLine]!!
    }

    override fun onCleared() {
        countDownTimer.reset()
        super.onCleared()
    }

    inner class Timer(
        private val millisInFuture: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        private var timeIsRunningOut = false
        private var currentTime = 0
        private var progressTime = 0

        override fun onTick(millisUntilFinished: Long) {
            currentTime = (millisInFuture - millisUntilFinished).toInt()
            progressTime = ((millisUntilFinished + 500) / 1000).toDouble().roundToInt()

            if (!timeIsRunningOut && millisUntilFinished <= 3000) {
                timeIsRunningOut = true
            }

            countDownCallback?.invoke(
                currentTime,
                progressTime,
                millisInFuture.toInt(),
                timeIsRunningOut,
                false
            )
        }

        override fun onFinish() {
            countDownCallback?.invoke(
                (millisInFuture).toInt(),
                0,
                0,
                timeIsRunningOut,
                true
            )
            reset()
        }

        fun addTime() {
            cancel()
            val time = (progressTime.toLong() * 1000) + PROGRESS_TIME_EXTRA
            countDownTimer = Timer(time, DEFAULT_PROGRESS_INTERVAL)
            countDownTimer.start()
        }

        fun reset() {
            cancel()
            countDownTimer = Timer(DEFAULT_PROGRESS_MAX_TIME, DEFAULT_PROGRESS_INTERVAL)
        }
    }
}
