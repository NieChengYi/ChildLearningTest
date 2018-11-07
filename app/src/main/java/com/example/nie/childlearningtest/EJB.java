package com.example.nie.childlearningtest;

public class EJB {

    private int mIndex;

    private static EJB sEJB = null;

    private EJB() {
        mIndex = 0;
    }

    public static EJB getInstance() {
        if (sEJB == null) {
            synchronized (EJB.class) {
                if (sEJB == null) {
                    sEJB = new EJB();
                }
            }
        }

        return sEJB;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }
}
