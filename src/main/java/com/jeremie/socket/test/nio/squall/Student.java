package com.jeremie.socket.test.nio.squall;

import java.io.Serializable;

/**
 * Created by jeremie on 15/5/16.
 */
public class Student implements Serializable {

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String name;

}
