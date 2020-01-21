package com.roma.berendeev.studysql

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roma.berendeev.studysql.domain.Singer
import com.roma.berendeev.studysql.domain.Song
import com.roma.berendeev.studysql.sqlite.SQLiteDbHelper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_song.*

private const val VIEW_TYPE_HEADER = -1
private const val VIEW_TYPE_SONG = -2
class SingerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var singerList: List<Singer> = emptyList()
    set(value) {
        if (value != field) {
            field = value
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent)
            VIEW_TYPE_SONG -> SongViewHolder(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount(): Int {
        return singerList.count() + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        return VIEW_TYPE_SONG
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SongViewHolder -> holder.bind(singerList[position - 1])
            is HeaderViewHolder -> holder.bind()
            else -> throw IllegalStateException()
        }
    }

    class SongViewHolder(parent: ViewGroup):
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_song)),
        LayoutContainer {

        override val containerView: View = itemView

        fun bind(singer: Singer) {
            idView.text = singer.id.toString()
            nameView.text = singer.name
        }
    }

    class HeaderViewHolder(parent: ViewGroup):
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_song)),
        LayoutContainer {

        override val containerView: View = itemView

        fun bind() {
            idView.text = SQLiteDbHelper.ID
            nameView.text = SQLiteDbHelper.SINGER_NAME
        }
    }
}
