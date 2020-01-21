package com.roma.berendeev.studysql

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_string.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.StringViewHolder>() {

    var stringList: List<String> = emptyList()
    set(value) {
        if (value != field) {
            field = value
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        return StringViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return stringList.count()
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.bind(stringList[position])
    }

    inner class StringViewHolder(parent: ViewGroup):
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_string)),
        LayoutContainer {

        override val containerView: View = itemView

        fun bind(string: String) {
            itemTextView.text = string
        }
    }
}
