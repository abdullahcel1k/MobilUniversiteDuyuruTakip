package com.example.abdullah.socialuniversity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abdullah on 3/10/17.
 */

public class PreferenceManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences dosya adı
    private static final String PREF_NAME = "SocialUniversity";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PreferenceManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    // cihazın ilk yüklemesi ise bu metot ile değişkenimizi true yapıyorz bir daha
    // başlangıç slideri gözükmemesi için
    public boolean isFirstTimeLaunch(){
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
