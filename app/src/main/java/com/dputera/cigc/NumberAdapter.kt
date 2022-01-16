package com.dputera.cigc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NumberAdapter( private var listNum: ArrayList<Int>, var choosenPos : Int?)
    :RecyclerView.Adapter<NumberAdapter.NumberHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NumberHolder {
        return NumberHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_number,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NumberHolder, position: Int) {
        holder.bindNumber(listNum[position])
    }

    override fun getItemCount(): Int {
        return listNum.size
    }

    inner class NumberHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var item: Int? = null
        val choosen = view.findViewById<TextView>(R.id.choosen)
        init {
            v.findViewById<RelativeLayout>(R.id.layout).setOnClickListener {
                if (view.context is MainActivity){
                    if (item!! <= 4) (view.context as MainActivity)
                        .setComment(v.context.getString(R.string.four_words), position)
                    else{
                        if (item!! <= 8) (view.context as MainActivity)
                            .setComment(v.context.getString(R.string.eight_words), position)
                        else {
                            if (item!! <= 12) (view.context as MainActivity)
                                .setComment(v.context.getString(R.string.twelve_words), position)
                            else{
                                if (item!! <= 16) (view.context as MainActivity)
                                    .setComment(v.context.getString(R.string.sixteen_words), position)
                                else{
                                    (view.context as MainActivity)
                                        .setComment(v.context.getString(R.string.twenty_words), position)
                                }
                            }
                        }
                    }
                }
            }
        }

        fun bindNumber(item: Int?) {
            if (item != null){
                this.item = item

                view.findViewById<TextView>(R.id.txt_number).text = item.toString()
                choosen.text = item.toString()

                if (choosenPos != null){
                    if (absoluteAdapterPosition == choosenPos){
                        choosen.visibility = View.VISIBLE
                    }
                }
            }
        }

    }
}