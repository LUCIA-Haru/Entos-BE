package com.lr.entos.shared.exception;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String messgae){
        super(messgae);
    }
}
