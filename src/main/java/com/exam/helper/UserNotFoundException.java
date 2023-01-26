package com.exam.helper;

public class UserNotFoundException extends Exception{
    public UserNotFoundException() {

        super("User with this username is not found in Database");
    }
    public UserNotFoundException(String msg) {super(msg);}
}
