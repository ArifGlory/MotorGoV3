package Kelas;

import android.content.Context;
import android.content.SharedPreferences;



/**
 * Created by Glory on 19/09/2018.
 */

public class UserPreference {
    private String KEY_JARAK = "jarakAman";

    private SharedPreferences preferences;

    public UserPreference(Context context){
        String PREFS_NAME = "UserPref";
        preferences = context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);
    }

    public void setJarakAman(int jarak){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_JARAK,jarak);
        editor.apply();
    }

   public int getJarakAman(){
        return preferences.getInt(KEY_JARAK,0);
    }
}
