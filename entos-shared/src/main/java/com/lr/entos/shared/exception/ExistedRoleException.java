package com.lr.entos.shared.exception;

public class ExistedRoleException extends RuntimeException{
    public ExistedRoleException(String message){
         super(message);
    }
}
