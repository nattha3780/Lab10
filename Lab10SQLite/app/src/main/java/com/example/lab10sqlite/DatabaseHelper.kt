package com.example.lab10sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID TEXT PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_AGE Integer)"
        db?.execSQL(CREATE_TABLE)

        val sqlInsert :String = "INSERT INTO $TABLE_NAME VALUES('1','Alice',20)"
        db?.execSQL(sqlInsert);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private val DB_NAME = "StudentDB"
        private val DB_VERSION = 1
        private val TABLE_NAME = "student"
        private val COLUMN_ID = "id"
        private val COLUMN_NAME = "name"
        private val COLUMN_AGE = "age"
    }
    fun getAllStudent(): ArrayList<Student> {
        val std = ArrayList<Student>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from $TABLE_NAME", null)
        } catch (e: SQLException) {
            onCreate(db)
            return ArrayList()
        }
        var id: String
        var name: String
        var age: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE))

                std.add(Student(id, name, age))
                cursor.moveToNext()
            }
        }
        db.close()
        return std
    }
    fun updateStudent(std:Student):Int{
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME ,std.name)
        values.put(COLUMN_AGE ,std.age)

        val success:Int = db.update(TABLE_NAME ,values ,"$COLUMN_ID = ?" , arrayOf(std.id))
        db.close()
        return success
    }

    fun insertStudent(std: Student): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, std.id)
        values.put(COLUMN_NAME, std.name)
        values.put(COLUMN_AGE, std.age)

        val success: Long = db.insert(TABLE_NAME, null, values)
        db.close()
        return success
    }


}