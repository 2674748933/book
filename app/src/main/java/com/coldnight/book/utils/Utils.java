package com.coldnight.book.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    List<String> stringList;

    public static void main(String[] args) {

        new Utils().init();
    }

    public void init() {
        stringList = new ArrayList<>();
        System.out.println(stringList);

        stringList.add("111");
        stringList.add("222");
        stringList.add("333");
        System.out.println(stringList);
    }



}
