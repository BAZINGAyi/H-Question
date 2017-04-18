package com.bazinga.async;

/**
 * Created by bazinga on 2017/4/17.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
