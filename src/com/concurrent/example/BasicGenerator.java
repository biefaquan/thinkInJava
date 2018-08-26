package com.concurrent.example;

import com.util.Generator;

/**
 * Created by bfq on 2018/8/26
 */
public class BasicGenerator {
    public static Generator create(Class cla) {
        return new Generator() {
            @Override
            public Object next() {
                Object obj = null;
                try {
                    System.out.println(cla.getName());
                    obj = Class.forName(cla.getName());
                } catch (Exception e) {
                    System.out.println("GG");
                }
                return obj;
            }
        };
    }
}
