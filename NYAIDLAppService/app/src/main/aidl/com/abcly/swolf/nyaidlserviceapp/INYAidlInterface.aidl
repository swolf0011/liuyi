// INYAidlInterface.aidl
package com.abcly.swolf.nyaidlserviceapp;

// Declare any non-default types here with import statements

interface INYAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    String sendMsg(String msg);

}