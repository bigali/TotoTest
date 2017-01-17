package com.example.shallak.todo.model;

/**
 * Created by shallak on 17/01/2017.
 */

public class GithubRepo {
    String name;
    String url;

    @Override
    public String toString() {
        return(name + " " +  url);
    }
}
