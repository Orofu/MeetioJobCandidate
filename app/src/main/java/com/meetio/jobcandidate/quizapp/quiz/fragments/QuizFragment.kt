package com.meetio.jobcandidate.quizapp.quiz.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.meetio.jobcandidate.quizapp.R
import com.meetio.jobcandidate.quizapp.quiz.QuizViewModel
import com.meetio.jobcandidate.quizapp.quiz.QuizViewModelFactory
import com.meetio.jobcandidate.quizapp.quiz.data.Answer
import com.meetio.jobcandidate.quizapp.replaceFragment
import kotlinx.android.synthetic.main.answer_layout.*
import kotlinx.android.synthetic.main.fragment_quiz.*

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private val incorrectButtonWithAnswers = arrayListOf<Button>()

    private val quizViewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(activity?.application!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity ?: return


        quizViewModel.newQuestion.observe(viewLifecycleOwner) {
            activity.replaceFragment(QuizFragment(), R.id.activity_quiz_container)
        }

        quizViewModel.quizComplete.observe(viewLifecycleOwner) {
            activity.replaceFragment(SummaryFragment(), R.id.activity_quiz_container)
        }

        quizViewModel.startCountDown { currentProgress, timeLeft, maxTime, timeRunningOut,
                                       finished ->
            quiz_progress_time.progress = currentProgress
            quiz_progress_time_text.text = timeLeft.toString()
            quiz_progress_time.max = maxTime

            if (finished) {
                quizViewModel.giveAnswer(Answer.createTimedOutAnswer(activity))
                return@startCountDown
            }

            if (timeRunningOut) {
                quiz_progress_time.progressDrawable.setColorFilter(
                    Color.RED,
                    PorterDuff.Mode.SRC_IN
                )
            }
        }

        // We have an image, load it from res
        if (quizViewModel.getQuestion().questionImage.isNotEmpty()) {
            quiz_img.visibility = View.VISIBLE
            val resId = resources.getIdentifier(
                quizViewModel.getQuestion().questionImage,
                "drawable", activity.packageName
            )
            val image = ContextCompat.getDrawable(activity, resId)
            quiz_img.setImageDrawable(image)
        } else {
            quiz_img.visibility = View.GONE
        }

        quiz_progress_text.text = getString(
            R.string.quiz_progress,
            quizViewModel.currentProgress,
            quizViewModel.questions.size
        )

        quiz_question.text = quizViewModel.getQuestion().questionText
        setupAnswers()
        setupLifeLines()
    }


    private fun setupLifeLines() {
        setupLifeLine(quiz_lifeLine_time, QuizViewModel.LifeLine.ADD_TIME) {}
        setupLifeLine(quiz_lifeLine_fifty_fifty, QuizViewModel.LifeLine.FIFTY_FIFTY) {
            useLifeLineFiftyFifty()
        }
    }

    private fun setupLifeLine(
        button: Button,
        lifeLine: QuizViewModel.LifeLine,
        callback: () -> Unit
    ) {
        button.run {
            setOnClickListener {
                quizViewModel.useLifeLine(lifeLine) {
                    callback.invoke()
                    disableButton(this)
                }
            }

            if (!quizViewModel.hasLifeLine(lifeLine)) {
                disableButton(this)
            }
        }
    }

    private fun setupAnswers() {
        val answers = quizViewModel.getAnswers()

        val listener: ((View) -> Unit) = {
            val answer = it.tag as Answer
            quizViewModel.giveAnswer(answer)
        }

        setupButtonWithAnswer(quiz_answer_1, answers[0], listener)
        setupButtonWithAnswer(quiz_answer_2, answers[1], listener)
        setupButtonWithAnswer(quiz_answer_3, answers[2], listener)
        setupButtonWithAnswer(quiz_answer_4, answers[3], listener)
    }

    private fun setupButtonWithAnswer(button: Button, answer: Answer, listener: ((View) -> Unit)) {
        button.run {
            text = answer.answer
            setOnClickListener(listener)
            tag = answer
            if (!answer.correct) incorrectButtonWithAnswers.add(this)
        }
    }

    private fun disableButton(button: Button) {
        button.isEnabled = false
        // TODO: Should use custom selector instead, add if time.
        val color = ContextCompat.getColor(requireActivity(), R.color.btn_disable)
        button.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
    }

    private fun useLifeLineFiftyFifty() {
        val removedAnswers = incorrectButtonWithAnswers.also { it.shuffle() }.take(2)
        for (btn: Button in removedAnswers) {
            btn.visibility = View.INVISIBLE
        }
    }
}