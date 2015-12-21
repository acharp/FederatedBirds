package fb.sio.ecp.fr.federatedbirds.utils;

/**
 * Created by charpi on 25/11/15.
 */
public class ValidationUtils {

    private static final String LOGIN_PATTERN = "^[A-Za-z0-9_-]{4,12}$";
    private static final String PASSWORD_PATTERN = "^\\w{4,12}$";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateLogin(String login){
        return login != null && login.matches(LOGIN_PATTERN);
    }

    public static boolean validatePassword(String password){
        return password != null && password.matches(PASSWORD_PATTERN);
    }

    public static boolean validateEmail(String email){
        return email != null && email.matches(EMAIL_PATTERN);
    }
}
