package com.example.arenaofpixels;

public class Person{
    private String nick;

    public Person(){

    }

    public Person(String nickname){
        nick = nickname;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}