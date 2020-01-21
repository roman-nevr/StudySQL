package com.roma.berendeev.studysql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val songAdapter = SongAdapter()
    private val listAdapter = ListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SongAdapter()
        fillList()
        rawEditField.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                fillList()
            }
            true
        }
        removeButton.setOnClickListener { rawEditField.text?.clear() }
    }

    private fun fillList() {
        if (rawEditField.text.isNullOrBlank()) {
            fillSongs()
        } else {
            fillStrings(rawEditField.text.toString())
        }
    }

    private fun fillSongs() {
        if (recyclerView.adapter != songAdapter) {
            recyclerView.adapter = songAdapter
        }
        (recyclerView.adapter as SongAdapter).songList = App.repository.getSongs()
    }

    private fun fillStrings(query: String) {
        if (recyclerView.adapter != listAdapter) {
            recyclerView.adapter = listAdapter
        }
        (recyclerView.adapter as ListAdapter).stringList = App.repository.query(query)
    }

}
