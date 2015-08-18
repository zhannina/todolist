package com.example.todolist;

/**
 * Created by tatoshka87 on 13/07/2015.
 */
public class ToDoElement {

    private int _id;
    private String _todo;

    public ToDoElement(){
        // empty constructor
        super();
    }

    public ToDoElement(int id, String todo){
        super();
        this._id = id;
        this._todo = todo;
    }

    public ToDoElement(String todo){
        super();
        this._todo = todo;
    }

    // getting id
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting todo
    public String getToDo(){
        return this._todo;
    }

    // setting todo
    public void setToDo(String todo){
        this._todo = todo;
    }

    @Override
    public String toString() {
        return  this._todo ;
    }

}
