package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatoshka87 on 13/07/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "toDoItems";

    // Contacts table name
    private static final String TABLE_TODOS = "todos";

    // Todos Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TODO = "todo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TODO + " TEXT" + ")";
        db.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public long addToDo(ToDoElement todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getToDo()); // todo

        // Inserting Row
        long id = db.insert(TABLE_TODOS, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public ToDoElement getToDo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ToDoElement todo = null;
        Log.d("id",String.valueOf(id));
        Cursor cursor = db.query(TABLE_TODOS, new String[] { KEY_ID,
                        KEY_TODO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Log.d("cursor", cursor.getCount()+"");

        if(cursor.getCount() >= 1) {
            todo = new ToDoElement(cursor.getInt(0),
                    cursor.getString(1));
            // return todotask
        }
        return todo;
    }

    public List<ToDoElement> getAllToDos() {
        List<ToDoElement> todoList = new ArrayList<ToDoElement>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ToDoElement todo = new ToDoElement();

                todo.setID(Integer.parseInt(cursor.getString(0)));
                todo.setToDo(cursor.getString(1));
                // Adding contact to list
                todoList.add(todo);
            } while (cursor.moveToNext());
        }

        // return to-do list
        return todoList;
    }

    // Getting contacts Count
    public int getTodosCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single contact
    public int updateToDo(ToDoElement todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getToDo());

        // updating row
        return db.update(TABLE_TODOS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getID()) });
    }

    // Deleting single contact
    public void deleteToDo(ToDoElement todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOS, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getID()) });
        db.close();
    }
}
