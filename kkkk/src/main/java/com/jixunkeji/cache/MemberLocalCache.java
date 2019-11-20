package com.jixunkeji.cache;


import com.jixunkeji.entity.Member;

/**
 */
public class MemberLocalCache {
    private static ThreadLocal<Member> threadLocal = new ThreadLocal<Member>();

    public static void set(Member member) {
        threadLocal.set(member);
    }

    @SuppressWarnings("unchecked")
    public static Member get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
