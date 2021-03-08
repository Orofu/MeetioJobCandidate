package com.meetio.jobcandidate.quizapp.quiz.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.meetio.jobcandidate.quizapp.R
import com.meetio.jobcandidate.quizapp.quiz.data.Answer
import com.meetio.jobcandidate.quizapp.quiz.data.Question
import kotlinx.android.synthetic.main.summary_list_item.view.*

class SummaryAdapter(
    context: Context,
    private val layoutId: Int,
    items: List<Pair<Question, Answer>>
) : ArrayAdapter<Pair<Question, Answer>>(context, layoutId, items) {

    override
    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutId, parent, false)

        val item = getItem(position)
        if (item != null) {
            val question = item.first
            val answer = item.second
            view.summary_list_item_question.text = question.questionText

            view.summary_list_item_answer.text = answer.answer

            val colorId = if (answer.correct) R.color.colorPrimary
                else android.R.color.holo_red_light
            view.summary_list_item_correct.setTextColor(ContextCompat.getColor(context, colorId))

            val textId = if (answer.correct) R.string.summary_result_is_correct
                else R.string.summary_result_is_incorrect
            view.summary_list_item_correct.text = context.getString(textId)
        }
        return view
    }
}