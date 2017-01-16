package com.example.shallak.todo.model;

import io.realm.RealmObject;

/**
 * Created by shallak on 16/01/2017.
 */

public class Todo extends RealmObject {
    private long id;
    private String text;

    public Todo(){

    }


    public Todo(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
