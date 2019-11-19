package com.example.projet

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuestionDB(context: Context,
                 factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAMEQuestion + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME
                + " TEXT" + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEQuestion)
        onCreate(db)
    }
    fun addQuestion(name: String) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        val db = this.writableDatabase
        db.insert(TABLE_NAMEQuestion, null, values)
        db.close()
    }
    fun getAllName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAMEQuestion", null)
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Quizz.db"
        val TABLE_NAMEQuestion = "Questions"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "question"
    }
}