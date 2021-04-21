package com.example.arenaofpixels;

import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.HashMap;

public class Resources {

    public static final String[] sections = {"Живопись", "Мемасики", "Фотография", "Скриншоты из игр"};

    public static String email;
    public static long currentNum;
    public static HashMap<String, String> imageMap;

    public static void setEmail(String em){
        email = em;
    }
    public static void setCurrentNum(long curNum){ currentNum = curNum; }
}
