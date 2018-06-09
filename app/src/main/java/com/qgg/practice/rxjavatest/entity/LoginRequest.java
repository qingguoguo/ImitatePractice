package com.qgg.practice.rxjavatest.entity;


public class LoginRequest {

    String cardno;
    String dtype;
    String key;

    public LoginRequest(String cardno, String dtype, String key) {
        this.cardno = cardno;
        this.dtype = dtype;
        this.key = key;
    }
}
