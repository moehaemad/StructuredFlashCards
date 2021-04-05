package com.moehaemad.structuredflashcards.controller;

public class User {
    protected String login;
    protected String password;

    /**
     * Empty constructor but not default Object.
     * */
    public User(){
        this.login = "";
        this.password = "";
    }

    /**
     * Constructor given information about user.
     * */
    public User (String username, String password){
        this.login = username;
        this.password = password;
    }

    /**
     * Check if user exists, true if username is not empty string.
     * */
    public boolean doesUserExist(){
        return !this.login.equals("");
    }

    /**
     * Simple getter for username.
     * */
    public String getUsername(){
        return this.login;
    }

    /**
     * Check if the information provided is empty at not usable.
     * */
    public void checkEmptyLogin(){
        if (this.login == "" || this.password == ""){
            throw new Error ("Error in User login: no username or password");
        }
    }

    /**
     * Used for testing purposes primarily but can be accessed where necessary.
     * */
    public void setLogin (String username){
        this.login = username;
    }

    /**
     * Same as previous method, different signature.
     * */
    public void setLogin (String username, String password){
        this.login = username;
        this.password = password;
    }


}
