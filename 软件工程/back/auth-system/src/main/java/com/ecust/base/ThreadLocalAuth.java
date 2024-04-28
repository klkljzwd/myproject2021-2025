package com.ecust.base;

import com.ecust.base.entity.Authority;

import java.util.List;

public class ThreadLocalAuth {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static Long get(){
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }
    public static void set(Long id){
        threadLocal.set(id);
    }
}
