package com.roma.berendeev.studysql.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.roma.berendeev.studysql.domain.Song

class SQLiteDbHelper: SQLiteOpenHelper {

    companion object {
        const val SONGS_TABLE_NAME = "songs"
        const val ID = BaseColumns._ID
        const val SONG_NAME = "name"
        const val ALBUM_NAME = "album"
        const val SONG_SINGER_ID = "singerId"
        const val SINGERS_TABLE_NAME = "singers"
        const val SINGER_NAME = "name"
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(
        context,
        name,
        factory,
        version
    )

    constructor(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int,
        errorHandler: DatabaseErrorHandler?
    ) : super(context, name, factory, version, errorHandler)

    constructor(context: Context?, name: String?, version: Int, openParams: SQLiteDatabase.OpenParams) : super(
        context,
        name,
        version,
        openParams
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        db.execSQL("CREATE TABLE $SINGERS_TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$SINGER_NAME TEXT NON NULL" +
                ");")
        db.execSQL("CREATE TABLE $SONGS_TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$SONG_NAME TEXT NON NULL, " +
                "$ALBUM_NAME TEXT NON NULL, " +
                "$SONG_SINGER_ID INTEGER NON NULL, " +
                "FOREIGN KEY($SONG_SINGER_ID) REFERENCES $SINGERS_TABLE_NAME($ID)" +
                ");")
        try {
            insertSong(db,"lalala", "lalka", 1)
            insertSong(db,"ulululu", "gang", 1)
            db.setTransactionSuccessful()
        } catch (e: Throwable) {

        }
        db.endTransaction()
    }

    override fun onUpgrade(db: SQLiteDatabase, StringoldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $SONGS_TABLE_NAME")
        onCreate(db)
    }

    fun insertSong(db: SQLiteDatabase, name: String, album: String, singerId: Long) {
        val contentValues = ContentValues()
        contentValues.put(SONG_NAME, name)
        contentValues.put(ALBUM_NAME, album)
        contentValues.put(SONG_SINGER_ID, singerId)
        db.insert(SONGS_TABLE_NAME, null, contentValues)
    }

    fun getSongs(db: SQLiteDatabase): List<Song> {
        val cursor = db.query(SONGS_TABLE_NAME, arrayOf(ID, SONG_NAME, ALBUM_NAME, SONG_SINGER_ID), null, null, null, null, null)
        if (cursor.count == 0) {
            return emptyList()
        }
        val values = arrayListOf<Song>()
        val idIndex = cursor.getColumnIndex(ID)
        val nameIndex = cursor.getColumnIndex(SONG_NAME)
        val albumIndex = cursor.getColumnIndex(ALBUM_NAME)
        val singerIdIndex = cursor.getColumnIndex(SONG_SINGER_ID)
        while (cursor.moveToNext()) {
            values.add(Song(
                cursor.getLong(idIndex),
                cursor.getString(nameIndex),
                cursor.getString(albumIndex),
                cursor.getLong(singerIdIndex)
            ))
        }
        cursor.close()
        return values
    }

    fun removeSong(db: SQLiteDatabase, songId: Long): Boolean {
        val count = db.delete(SONGS_TABLE_NAME, "$ID = $songId", null)
        return count > 0
    }

    fun rawQuery(db: SQLiteDatabase, query: String): List<String> {
        return when  {
            query.startsWith("insert", true) -> listOf(insertQuery(db, query))
            query.startsWith("update", true) -> listOf(updateQuery(db, query))
            query.startsWith("delete", true) -> listOf(removeQuery(db, query))
            query.startsWith("select", true) -> selectQuery(db, query)
            else -> listOf(executeQuery(db, query))
        }
    }

    private fun selectQuery(db: SQLiteDatabase, query: String): List<String> {
        try {
            db.rawQuery(query, null).use { cursor ->
                if (cursor.count == 0) {
                    return listOf("Ничего не найдено")
                }
                val values = arrayListOf<String>()
                val cursorNameMap = cursor.columnNames
                    .map { name -> name to cursor.getColumnIndex(name) }
                    .toMap()
                while (cursor.moveToNext()) {
                    val string = cursorNameMap.entries.joinToString(separator = ", ") { entry ->
                        "${entry.key}: " + cursor.getString(entry.value)
                    }
                    values.add(string)
                }
                cursor.close()
                return values
            }
        } catch (error: Throwable) {
            return listOf(error.message.orEmpty())
        }
    }

    private fun insertQuery(db: SQLiteDatabase, query: String): String {
        db.beginTransaction()
        return try {
            db.compileStatement(query).use { statement ->
                statement.executeInsert()
            }
            db.setTransactionSuccessful()
            db.endTransaction()
            "Вставка выполнена успешно"
        } catch (error: Exception) {
            db.endTransaction()
            "Ошибка вставки: " + error.message.orEmpty()
        }
    }

    private fun removeQuery(db: SQLiteDatabase, query: String): String {
        db.beginTransaction()
        return try {
            val deleteCount = db.compileStatement(query).executeUpdateDelete()
            db.setTransactionSuccessful()
            db.endTransaction()
            "Удалено $deleteCount записей"
        } catch (error: Exception) {
            db.endTransaction()
            "Ошибка удаления: "+error.message.orEmpty()
        }
    }

    private fun updateQuery(db: SQLiteDatabase, query: String): String {
        db.beginTransaction()
        return try {
            val updateCount = db.compileStatement(query).executeUpdateDelete()
            db.setTransactionSuccessful()
            db.endTransaction()
            "Обновлено $updateCount записей"
        } catch (error: Exception) {
            db.endTransaction()
            "Ошибка обновления: "+error.message.orEmpty()
        }
    }

    private fun executeQuery(db: SQLiteDatabase, query: String): String {
        db.beginTransaction()
        return try {
            db.compileStatement(query).execute()
            db.setTransactionSuccessful()
            db.endTransaction()
            "Выполнено"
        } catch (error: Exception) {
            db.endTransaction()
            "Ошибка в запросе: "+error.message.orEmpty()
        }
    }
}
