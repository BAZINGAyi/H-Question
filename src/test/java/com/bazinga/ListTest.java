package com.bazinga;

import com.bazinga.async.EventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bazinga on 2017/4/17.
 */
public class ListTest {
    public static void main(String[] args){

        List<EventType> list = Arrays.asList(EventType.LIKE,EventType.COMMENT_OTHER);
        for(EventType type:list){
            System.out.println(type.getValue());
        }
    }
}
