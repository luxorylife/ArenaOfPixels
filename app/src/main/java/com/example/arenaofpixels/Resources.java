package com.example.arenaofpixels;

import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.HashMap;

public class Resources {

    public static final String[] sections = {"Живопись", "Мемасики", "Фотография", "Скриншоты из игр"};

    public static String email;
    public static String nickname;

    public static int currentNum;
    public static ArrayList<ImageObj> imageMap;

    public static ArrayList<String> users;
    public static long numUsers;

    public static void setEmail(String em){
        email = em;
    }
    public static void setNickname(String nick) { nickname = nick;}

    public static void setCurrentNum(int curNum){ currentNum = curNum; }
    public static void setUsers(ArrayList<String> emails){
        users = emails;
        numUsers = emails.size();
    }
}
