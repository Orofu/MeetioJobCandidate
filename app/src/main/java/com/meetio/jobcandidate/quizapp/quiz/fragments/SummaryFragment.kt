package com.meetio.jobcandidate.quizapp.quiz.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.meetio.jobcandidate.quizapp.R
import com.meetio.jobcandidate.quizapp.quiz.QuizViewModel
import com.meetio.jobcandidate.quizapp.quiz.QuizViewModelFactory
import com.meetio.jobcandidate.quizapp.quiz.adapters.SummaryAdapter
import kotlinx.android.synthetic.main.fragment_summary.*

class SummaryFragment : Fragment(R.layout.fragment_summary) {
    private val quizViewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(activity?.application!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity ?: return

        val result = quizViewModel.getResult()

        val adapter = SummaryAdapter(activity, R.layout.summary_list_item, result)
        summary_list.adapter = adapter
    }
}