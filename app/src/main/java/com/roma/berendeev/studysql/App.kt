package com.roma.berendeev.studysql

import android.app.Application
import com.roma.berendeev.studysql.sqlite.Repository
import com.roma.berendeev.studysql.sqlite.SQLiteDbHelper

class App: Application() {


    override fun onCreate() {
        super.onCreate()
        repository = Repository(this)
    }

    companion object {
        lateinit var repository: Repository
    }
}
