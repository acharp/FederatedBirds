package fb.sio.ecp.fr.federatedbirds.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by charpi on 25/11/15.
 */
public class TokenManager {

    private static final String AUTH_PREFERENCES="auth";
    private static final String TOKEN_KEY = "token";

    public static String getUserToken(Context context){
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(TOKEN_KEY, null);
    }

    public static void setUserToken(Context context, String token){
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(TOKEN_KEY, token).apply();
        Log.i(TokenManager.class.getSimpleName(), "User token saved: " + token);
    }

}
