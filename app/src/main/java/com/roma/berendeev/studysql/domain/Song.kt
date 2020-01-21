package com.roma.berendeev.studysql.domain

data class Song(val id: Long, val name: String, val album: String, val singerId: Long)

data class Singer(val id: Long, val name: String)
