package com.roma.berendeev.studysql.sqlite

import android.content.Context
import com.roma.berendeev.studysql.domain.Song

class Repository(context: Context) {

    private val openHelper = SQLiteDbHelper(context, "DATA_BASE", null, 3)
    private val database = openHelper.writableDatabase

    fun insertSong(song: Song) {
        openHelper.insertSong(database, song.name, song.album, song.singerId)
    }

    fun getSongs(): List<Song> {
        return openHelper.getSongs(database)
    }

    fun remove(songId: Long): Boolean {
        return openHelper.removeSong(database, songId)
    }

    fun query(query: String): List<String> {
        return openHelper.rawQuery(database, query)
    }

}
